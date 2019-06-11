
package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.Enrolment;

@Component
@Transactional
public class EnrolmentToStringConverter implements Converter<Enrolment, String> {

	@Override
	public String convert(final Enrolment enrolment) {
		String result;

		if (enrolment == null)
			result = null;
		else
			result = String.valueOf(enrolment.getId());

		return result;
	}

}
