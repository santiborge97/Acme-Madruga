
package controllers.actor;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import controllers.AbstractController;

import services.ActorService;
import services.AdministratorService;
import services.BrotherhoodService;
import services.ConfigurationService;
import services.MemberService;
import domain.Actor;
import domain.Administrator;
import domain.Brotherhood;
import domain.Member;

@Controller
@RequestMapping("/actor")
public class ActorController extends AbstractController {

	// Services-----------------------------------------------------

	@Autowired
	private ActorService			actorService;

	@Autowired
	private MemberService			memberService;

	@Autowired
	private BrotherhoodService		brotherhoodService;

	@Autowired
	private AdministratorService	administratorService;

	@Autowired
	private ConfigurationService	configurationService;


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;

		final Actor actor = this.actorService.findByPrincipal();

		Collection<Member> members;
		Collection<Brotherhood> brotherhoods;
		Collection<Administrator> administrators;

		members = this.memberService.findAll();
		brotherhoods = this.brotherhoodService.findAll();
		administrators = this.administratorService.findAll();

		if (members.contains(actor))
			members.remove(actor);
		else if (brotherhoods.contains(actor))
			brotherhoods.remove(actor);
		else if (administrators.contains(actor))
			administrators.remove(actor);

		final String banner = this.configurationService.findConfiguration().getBanner();

		result = new ModelAndView("actor/list");
		result.addObject("members", members);
		result.addObject("brotherhoods", brotherhoods);
		result.addObject("administrators", administrators);
		result.addObject("banner", banner);

		result.addObject("requestURI", "actor/list.do");

		return result;

	}

}
