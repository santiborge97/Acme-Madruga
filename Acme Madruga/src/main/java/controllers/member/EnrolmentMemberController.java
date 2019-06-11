
package controllers.member;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
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
import domain.Brotherhood;
import domain.Enrolment;
import domain.Member;

@Controller
@RequestMapping("/enrolment/member")
public class EnrolmentMemberController extends AbstractController {

	@Autowired
	private BrotherhoodService		brotherhoodService;

	@Autowired
	private MemberService			memberService;

	@Autowired
	private ConfigurationService	configurationService;

	@Autowired
	private EnrolmentService		enrolmentService;

	@Autowired
	private MessageService			messageService;


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {

		final ModelAndView result;
		final Collection<Enrolment> enrolments;
		final Member m;

		m = this.memberService.findByPrincipal();

		enrolments = this.enrolmentService.findEnrolmentsByMemberId(m.getId());

		final String banner = this.configurationService.findConfiguration().getBanner();

		result = new ModelAndView("enrolment/list");
		result.addObject("enrolments", enrolments);
		result.addObject("requestURI", "enrolment/member/list.do");
		result.addObject("pagesize", 5);
		result.addObject("banner", banner);
		result.addObject("language", LocaleContextHolder.getLocale().getLanguage());
		result.addObject("autoridad", "member");

		return result;

	}

	@RequestMapping(value = "/enrol", method = RequestMethod.GET)
	public ModelAndView enrol(@RequestParam final int brotherhoodId) {
		ModelAndView result;

		final Member member = this.memberService.findByPrincipal();

		final Boolean security = this.enrolmentService.findEnrolmentsByMemberIdAndBrotherhood(member.getId(), brotherhoodId);

		if (security) {

			final Enrolment enrolment = this.enrolmentService.create(brotherhoodId);
			try {
				this.enrolmentService.save(enrolment);
				result = new ModelAndView("redirect:/enrolment/member/list.do");
			} catch (final Throwable oops) {
				result = new ModelAndView("redirect:/welcome/index.do");
			}
		} else {
			result = new ModelAndView("misc/alreadyEnrol");
			final String banner = this.configurationService.findConfiguration().getBanner();
			result.addObject("banner", banner);

		}

		return result;
	}

	@RequestMapping(value = "/dropout", method = RequestMethod.GET)
	public ModelAndView dropOut(@RequestParam final int enrolmentId) {
		ModelAndView result;
		final Enrolment enrolment = this.enrolmentService.findOne(enrolmentId);

		try {
			Assert.isTrue(this.enrolmentService.enrolmentMemberSecurity(enrolmentId) && this.enrolmentService.enrolmentBrotherhoodSecurity(enrolmentId));
			final Date currentMoment = new Date(System.currentTimeMillis() - 1000);
			enrolment.setDropOutMoment(currentMoment);

			final Brotherhood brotherhood = this.brotherhoodService.findOne(enrolment.getBrotherhood().getId());

			final Collection<Member> members = brotherhood.getMembers();
			members.remove(this.memberService.findOne(enrolment.getMember().getId()));
			brotherhood.setMembers(members);

			this.brotherhoodService.editMemberList(brotherhood);

			this.enrolmentService.save(enrolment);

			this.messageService.NotificationDropOutMember(enrolment);

			result = new ModelAndView("redirect:/enrolment/member/list.do");

		} catch (final Throwable oops) {

			result = new ModelAndView("redirect:/welcome/index.do");

		}
		return result;
	}

}
