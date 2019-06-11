
package controllers.brotherhood;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import services.BoxService;
import services.BrotherhoodService;
import services.FloatService;
import services.MessageService;
import services.ProcessionService;
import services.SocialProfileService;
import domain.Box;
import domain.Brotherhood;
import domain.Float;
import domain.Message;
import domain.Procession;
import domain.SocialProfile;

@Controller
@RequestMapping("/data/brotherhood")
public class DownloadDataBrotherhoodController {

	@Autowired
	private BrotherhoodService		brotherhoodService;
	@Autowired
	private SocialProfileService	socialProfileService;
	@Autowired
	private MessageService			messageService;
	@Autowired
	private BoxService				boxService;
	@Autowired
	private FloatService			floatService;
	@Autowired
	private ProcessionService		processionService;


	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public void test(final HttpSession session, final HttpServletResponse response) throws IOException {
		String myString = "Below these lines you can find all the data we have at Acme-Madrugá:\r\n";

		final Brotherhood m = this.brotherhoodService.findByPrincipal();
		final Collection<SocialProfile> sc = this.socialProfileService.findAllByActor(m.getId());
		final Collection<Message> msgs = this.messageService.messagePerActor(m.getId());
		final Collection<Box> bxs = this.boxService.findAllBoxByActor(m.getId());
		final Collection<Float> fs = this.floatService.findFloatsByBrotherhoodId(m.getId());
		final Collection<Procession> ens = this.processionService.findProcessionByBrotherhoodId(m.getId());

		myString += "\r\n\r\n";

		myString += m.getName() + " " + m.getMiddleName() + " " + m.getSurname() + " " + m.getAddress() + " " + m.getEmail() + " " + m.getPhone() + " " + m.getPhoto() + " \r\n";
		myString += "\r\n\r\n";
		myString += "Social Profiles:\r\n";
		for (final SocialProfile s : sc)
			myString += s.getNick() + " " + s.getLink() + " " + s.getSocialName() + "\r\n";
		myString += "\r\n\r\n";
		myString += "Boxes:\r\n";
		for (final Box b : bxs)
			myString += "Box name: " + b.getName() + "\r\n";
		myString += "\r\n\r\n";
		myString += "Messages:\r\n\r\n";
		for (final Message msg : msgs)
			myString += "Sender: " + msg.getSender().getName() + " " + msg.getSender().getSurname() + " Recipient: " + msg.getRecipient().getName() + " " + msg.getRecipient().getSurname() + " Moment: " + msg.getMoment() + " Subject: " + msg.getSubject()
				+ " Body: " + msg.getBody() + " Tags: " + msg.getTags() + " Priority: " + msg.getPriority() + "\r\n";
		myString += "\r\n\r\n";
		myString += "Floats:\r\n\r\n";
		for (final Float f : fs)
			myString += "Title: " + f.getTitle() + " Description: " + f.getDescription() + " Pictures: " + Arrays.toString(f.getPictures().toArray()) + "\r\n";
		myString += "\r\n\r\n";
		myString += "Processions:\r\n\r\n";
		for (final Procession r : ens)
			myString += "Procession title: " + r.getTitle() + " Description: " + r.getDescription() + " Moment: " + r.getOrganisationMoment() + "\r\n";

		response.setContentType("text/plain");
		response.setHeader("Content-Disposition", "attachment;filename=my_data_as_brotherhood.txt");
		final ServletOutputStream out = response.getOutputStream();
		out.println(myString);
		out.flush();
		out.close();
	}

}
