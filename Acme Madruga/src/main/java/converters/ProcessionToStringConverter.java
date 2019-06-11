
package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.Procession;

@Component
@Transactional
public class ProcessionToStringConverter implements Converter<Procession, String> {

	@Override
	public String convert(final Procession procession) {
		String result;

		if (procession == null)
			result = null;
		else
			result = String.valueOf(procession.getId());

		return result;
	}
}
