
package controllers.brotherhood;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import controllers.AbstractController;

import services.BrotherhoodService;
import services.ConfigurationService;
import services.EnrolmentService;
import services.MemberService;
import services.MessageService;
import services.PositionService;
import domain.Brotherhood;
import domain.Enrolment;
import domain.Member;
import domain.Position;

@Controller
@RequestMapping("/enrolment/brotherhood")
public class EnrolmentBrotherhoodController extends AbstractController {

	@Autowired
	private BrotherhoodService		brotherhoodService;

	@Autowired
	private MemberService			memberService;

	@Autowired
	private ConfigurationService	configurationService;

	@Autowired
	private EnrolmentService		enrolmentService;

	@Autowired
	private PositionService			positionService;

	@Autowired
	private MessageService			messageService;


	@RequestMapping(value = "/listNoPosition", method = RequestMethod.GET)
	public ModelAndView listNoPosition() {

		final ModelAndView result;
		final Brotherhood b;

		b = this.brotherhoodService.findByPrincipal();

		final String banner = this.configurationService.findConfiguration().getBanner();

		if (b.getArea() == null) {
			result = new ModelAndView("misc/noArea");
			result.addObject("banner", banner);

		} else {

			final Collection<Enrolment> enrolments;

			enrolments = this.enrolmentService.findEnrolmentsByBrotherhoodIdNoPosition(b.getId());

			result = new ModelAndView("enrolment/list");
			result.addObject("enrolments", enrolments);
			result.addObject("requestURI", "enrolment/brotherhood/listNoPosition.do");
			result.addObject("pagesize", 5);
			result.addObject("banner", banner);
			result.addObject("language", LocaleContextHolder.getLocale().getLanguage());
			result.addObject("autoridad", "brotherhood");
			result.addObject("noPosition", true);
		}

		return result;

	}
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {

		final ModelAndView result;
		final Brotherhood b;
		b = this.brotherhoodService.findByPrincipal();

		final String banner = this.configurationService.findConfiguration().getBanner();

		if (b.getArea() == null) {
			result = new ModelAndView("misc/noArea");
			result.addObject("banner", banner);
		} else {

			final Collection<Enrolment> enrolments;

			enrolments = this.enrolmentService.findEnrolmentsByBrotherhoodId(b.getId());

			result = new ModelAndView("enrolment/list");
			result.addObject("enrolments", enrolments);
			result.addObject("requestURI", "enrolment/brotherhood/list.do");
			result.addObject("pagesize", 5);
			result.addObject("banner", banner);
			result.addObject("language", LocaleContextHolder.getLocale().getLanguage());
			result.addObject("autoridad", "brotherhood");
			result.addObject("noPosition", false);
		}

		return result;

	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int enrolmentId) {
		ModelAndView result;
		Enrolment enrolment;
		Boolean security;

		final Brotherhood b;
		b = this.brotherhoodService.findByPrincipal();

		if (b.getArea() == null)
			result = new ModelAndView("misc/noArea");
		else {
			enrolment = this.enrolmentService.findOne(enrolmentId);
			security = this.enrolmentService.enrolmentBrotherhoodSecurity(enrolmentId);

			if (security)
				result = this.createEditModelAndView(enrolment, null);
			else
				result = new ModelAndView("redirect:/welcome/index.do");

		}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@ModelAttribute(value = "enrolment") Enrolment enrolment, final BindingResult binding) {
		ModelAndView result;

		final Brotherhood b;
		b = this.brotherhoodService.findByPrincipal();

		if (b.getArea() == null)
			result = new ModelAndView("misc/noArea");
		else {

			enrolment = this.enrolmentService.reconstruct(enrolment, binding);

			if (binding.hasErrors())
				result = this.createEditModelAndView(enrolment, null);
			else
				try {
					final Brotherhood brotherhood = this.brotherhoodService.findByPrincipal();

					final Collection<Member> members = brotherhood.getMembers();

					if (!members.contains(enrolment.getMember())) {
						members.add(this.memberService.findOne(enrolment.getMember().getId()));
						brotherhood.setMembers(members);
					}

					this.brotherhoodService.editMemberList(brotherhood);

					this.enrolmentService.save(enrolment);

					this.messageService.NotificationNewEnrolment(enrolment);

					result = new ModelAndView("redirect:/welcome/index.do");
				} catch (final Throwable oops) {
					result = this.createEditModelAndView(enrolment, "enrolment.commit.error");

				}

		}

		return result;
	}

	@RequestMapping(value = "/dropout", method = RequestMethod.GET)
	public ModelAndView dropOut(@RequestParam final int enrolmentId) {
		ModelAndView result;
		final Enrolment enrolment = this.enrolmentService.findOne(enrolmentId);

		final Brotherhood b;
		b = this.brotherhoodService.findByPrincipal();

		if (b.getArea() == null)
			result = new ModelAndView("misc/noArea");
		else {

			final Date currentMoment = new Date(System.currentTimeMillis() - 1000);
			enrolment.setDropOutMoment(currentMoment);

			try {

				final Brotherhood brotherhood = this.brotherhoodService.findOne(enrolment.getBrotherhood().getId());

				final Collection<Member> members = brotherhood.getMembers();
				members.remove(this.memberService.findOne(enrolment.getMember().getId()));
				brotherhood.setMembers(members);

				this.brotherhoodService.editMemberList(brotherhood);

				this.enrolmentService.save(enrolment);

				this.messageService.NotificationDropOutBrotherhood(enrolment);

				result = new ModelAndView("redirect:/enrolment/brotherhood/list.do");

			} catch (final Throwable oops) {

				result = new ModelAndView("redirect:/welcome/index.do");

			}

		}
		return result;
	}

	private ModelAndView createEditModelAndView(final Enrolment enrolment, final Object object) {
		ModelAndView result;
		final Collection<Position> positions;

		positions = this.positionService.findAll();

		final String banner = this.configurationService.findConfiguration().getBanner();

		result = new ModelAndView("enrolment/edit");
		result.addObject("enrolment", enrolment);
		result.addObject("positions", positions);
		result.addObject("banner", banner);
		result.addObject("messageError", object);
		result.addObject("language", LocaleContextHolder.getLocale().getLanguage());

		return result;
	}
}
