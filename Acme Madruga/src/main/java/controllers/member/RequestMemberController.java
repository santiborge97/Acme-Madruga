
package controllers.member;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ConfigurationService;
import services.MemberService;
import services.ProcessionService;
import services.RequestService;
import controllers.AbstractController;
import domain.Member;
import domain.Procession;
import domain.Request;

@Controller
@RequestMapping("/request/member")
public class RequestMemberController extends AbstractController {

	// Services
	@Autowired
	private RequestService			requestService;

	@Autowired
	private MemberService			memberService;

	@Autowired
	private ProcessionService		processionService;

	@Autowired
	private ConfigurationService	configurationService;


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView result;
		final Collection<Request> requests;
		final Member m;

		m = this.memberService.findByPrincipal();

		requests = this.requestService.findRequestsByMemberId(m.getId());

		final String banner = this.configurationService.findConfiguration().getBanner();

		result = new ModelAndView("request/list");
		result.addObject("requests", requests);
		result.addObject("requestURI", "request/member/list.do");
		result.addObject("pagesize", 5);
		result.addObject("banner", banner);
		result.addObject("language", LocaleContextHolder.getLocale().getLanguage());

		return result;

	}

	@RequestMapping(value = "/listProcessions", method = RequestMethod.GET)
	public ModelAndView listProcessions() {
		final ModelAndView result;
		final Collection<Procession> processions;
		final Member m;

		m = this.memberService.findByPrincipal();

		processions = this.processionService.findMemberProcessions(m.getId());

		final String banner = this.configurationService.findConfiguration().getBanner();

		result = new ModelAndView("request/listProcessions");
		result.addObject("processions", processions);
		result.addObject("requestURI", "request/member/listProcessions.do");
		result.addObject("pagesize", 5);
		result.addObject("banner", banner);
		result.addObject("language", LocaleContextHolder.getLocale().getLanguage());

		return result;

	}

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView show(@RequestParam final int requestId) {
		ModelAndView result;
		final Request request;
		final Member owner;

		try {
			owner = this.memberService.findByPrincipal();
			request = this.requestService.findOne(requestId);

			if (request.getMember().getId() == owner.getId()) {
				final String banner = this.configurationService.findConfiguration().getBanner();

				result = new ModelAndView("request/display");
				result.addObject("request", request);
				result.addObject("banner", banner);

			} else
				result = new ModelAndView("redirect:/welcome/index.do");
		} catch (final IllegalArgumentException e) {
			result = new ModelAndView("misc/notExist");
		}

		return result;
	}

	@RequestMapping(value = "/march", method = RequestMethod.GET)
	public ModelAndView march(@RequestParam final int processionId) {
		ModelAndView result;
		final Request request;
		final Member owner;
		if (!this.requestService.hasAcceptedOrPendingRequestsOfMemberIn(this.memberService.findByPrincipal().getId(), processionId))
			try {

				owner = this.memberService.findByPrincipal();
				final Procession procession = this.processionService.findOne(processionId);

				final Collection<Procession> processions = this.processionService.findMemberProcessions(owner.getId());

				if (processions.contains(procession)) {

					request = this.requestService.create();

					request.setMember(owner);
					request.setProcession(procession);
					request.setStatus("PENDING");

					this.requestService.save(request);

					result = new ModelAndView("redirect:/request/member/list.do");

				} else
					result = new ModelAndView("redirect:/welcome/index.do");
			} catch (final IllegalArgumentException e) {
				result = new ModelAndView("misc/notExist");
			}
		else
			result = new ModelAndView("redirect:/request/member/list.do");

		return result;
	}
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int requestId) {
		ModelAndView result;
		final Request request;
		final Member owner;

		try {

			owner = this.memberService.findByPrincipal();
			request = this.requestService.findOne(requestId);

			if (request.getMember().getId() == owner.getId() && request.getStatus().equals("PENDING")) {

				this.requestService.delete(request);

				result = new ModelAndView("redirect:/request/member/list.do");
			} else
				result = new ModelAndView("redirect:/welcome/index.do");
		} catch (final IllegalArgumentException e) {
			result = new ModelAndView("misc/notExist");
		}

		return result;
	}
}
