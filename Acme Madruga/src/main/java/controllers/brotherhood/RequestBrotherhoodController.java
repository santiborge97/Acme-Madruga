
package controllers.brotherhood;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.BrotherhoodService;
import services.ConfigurationService;
import services.MessageService;
import services.RequestService;
import controllers.AbstractController;
import domain.Brotherhood;
import domain.Request;

@Controller
@RequestMapping("/request/brotherhood")
public class RequestBrotherhoodController extends AbstractController {

	// Services
	@Autowired
	private RequestService			requestService;

	@Autowired
	private BrotherhoodService		brotherhoodService;

	@Autowired
	private ConfigurationService	configurationService;

	@Autowired
	private MessageService			messageService;


	@RequestMapping(value = "/listPending", method = RequestMethod.GET)
	public ModelAndView listPending() {
		final ModelAndView result;
		final Collection<Request> requests;
		final Brotherhood b;

		b = this.brotherhoodService.findByPrincipal();

		requests = this.requestService.findPendingRequestsByBrotherhoodId(b.getId());

		final String banner = this.configurationService.findConfiguration().getBanner();

		result = new ModelAndView("request/listPending");
		result.addObject("requests", requests);
		result.addObject("requestURI", "request/brotherhood/listPending.do");
		result.addObject("pagesize", 5);
		result.addObject("banner", banner);
		result.addObject("language", LocaleContextHolder.getLocale().getLanguage());

		return result;

	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView result;
		final Collection<Request> requests;
		final Brotherhood b;

		b = this.brotherhoodService.findByPrincipal();

		requests = this.requestService.findFinalRequestsByBrotherhoodId(b.getId());

		final String banner = this.configurationService.findConfiguration().getBanner();

		result = new ModelAndView("request/list");
		result.addObject("requests", requests);
		result.addObject("requestURI", "request/brotherhood/list.do");
		result.addObject("pagesize", 5);
		result.addObject("banner", banner);
		result.addObject("language", LocaleContextHolder.getLocale().getLanguage());

		return result;

	}

	@RequestMapping(value = "/reject", method = RequestMethod.GET)
	public ModelAndView reject(@RequestParam final int requestId) {
		ModelAndView result;
		final Request request;
		final Brotherhood owner;

		owner = this.brotherhoodService.findByPrincipal();
		try {
			request = this.requestService.findOne(requestId);

			if (request.getProcession().getBrotherhood().getId() == owner.getId()) {
				final String banner = this.configurationService.findConfiguration().getBanner();
				result = new ModelAndView("request/reject");
				result.addObject("request", request);
				result.addObject("banner", banner);
			} else
				result = new ModelAndView("redirect:/welcome/index.do");
		} catch (final IllegalArgumentException e) {
			result = new ModelAndView("misc/notExist");
		}
		return result;
	}

	@RequestMapping(value = "/reject", method = RequestMethod.POST, params = "reject")
	public ModelAndView reject(Request request, final BindingResult binding) {
		ModelAndView result;
		final Brotherhood owner;
		owner = this.brotherhoodService.findByPrincipal();
		try {
			final Request another = this.requestService.findOne(request.getId());
			if (another.getProcession().getBrotherhood().getId() == owner.getId()) {

				request = this.requestService.reconstruct(request, binding);

				if (request.getComment().equals(""))
					result = this.createEditModelAndView(request, "blank");
				else {

					request.setStatus("REJECTED");
					request.setColumnNumber(null);
					request.setRowNumber(null);
					request.setProcession(another.getProcession());
					request.setMember(another.getMember());

					if (binding.hasErrors())
						result = this.createEditModelAndView(request, null);
					else
						try {

							this.requestService.save(request);

							this.messageService.NotificationRequestStatus(request);

							result = new ModelAndView("redirect:/request/brotherhood/list.do");

						} catch (final Throwable oops) {
							result = this.createEditModelAndView(request, "error");
						}
				}

			} else
				result = new ModelAndView("redirect:/welcome/index.do");

		} catch (final IllegalArgumentException e) {
			result = new ModelAndView("misc/notExist");
		}

		return result;
	}
	@RequestMapping(value = "/accept", method = RequestMethod.GET)
	public ModelAndView accept(@RequestParam final int requestId) {
		ModelAndView result;
		Request request;
		final Brotherhood owner;
		try {
			owner = this.brotherhoodService.findByPrincipal();
			request = this.requestService.findOne(requestId);

			if (request.getProcession().getBrotherhood().getId() == owner.getId()) {
				final String banner = this.configurationService.findConfiguration().getBanner();
				request = this.requestService.suggestNextRow(request.getProcession().getId(), request);
				result = new ModelAndView("request/accept");
				result.addObject("request", request);
				result.addObject("banner", banner);

			} else
				result = new ModelAndView("redirect:/welcome/index.do");
		} catch (final IllegalArgumentException e) {
			result = new ModelAndView("misc/notExist");
		}
		return result;
	}
	@RequestMapping(value = "/accept", method = RequestMethod.POST, params = "accept")
	public ModelAndView accept(@ModelAttribute(value = "request") Request request, final BindingResult binding) {
		ModelAndView result;
		final Brotherhood owner;

		owner = this.brotherhoodService.findByPrincipal();
		request = this.requestService.reconstructAccept(request, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndViewAccept(request, null);
		else
			try {
				if (this.requestService.hasRequestIn(request.getColumnNumber(), request.getRowNumber(), request.getProcession().getId()))
					result = this.createEditModelAndViewAccept(request, "error.position");
				else if (request.getProcession().getBrotherhood().getId() == owner.getId()) {
					request.setStatus("APPROVED");
					this.requestService.save(request);

					this.messageService.NotificationRequestStatus(request);

					result = new ModelAndView("redirect:/request/brotherhood/list.do");

				} else
					result = new ModelAndView("redirect:/welcome/index.do");

			} catch (final Throwable oops) {
				result = this.createEditModelAndViewAccept(request, "error");
			}
		return result;
	}
	protected ModelAndView createEditModelAndViewAccept(final Request request, final String messageCode) {
		final ModelAndView result;

		final String banner = this.configurationService.findConfiguration().getBanner();

		result = new ModelAndView("request/accept");
		result.addObject("request", request);
		result.addObject("messageError", messageCode);
		result.addObject("banner", banner);
		result.addObject("language", LocaleContextHolder.getLocale().getLanguage());

		return result;

	}
	protected ModelAndView createEditModelAndView(final Request request, final String messageCode) {
		final ModelAndView result;

		final String banner = this.configurationService.findConfiguration().getBanner();

		result = new ModelAndView("request/reject");
		result.addObject("request", request);
		result.addObject("messageError", messageCode);
		result.addObject("banner", banner);
		result.addObject("language", LocaleContextHolder.getLocale().getLanguage());

		return result;

	}

}
