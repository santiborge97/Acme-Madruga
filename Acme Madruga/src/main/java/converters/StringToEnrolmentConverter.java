
package converters;

import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import repositories.EnrolmentRepository;
import domain.Enrolment;

@Component
@Transactional
public class StringToEnrolmentConverter implements Converter<String, Enrolment> {

	@Autowired
	private EnrolmentRepository	enrolmentRepository;


	@Override
	public Enrolment convert(final String text) {
		Enrolment result;
		final int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.enrolmentRepository.findOne(id);
			}
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}

}
