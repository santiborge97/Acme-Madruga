
package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.Brotherhood;

@Component
@Transactional
public class BrotherhoodToStringConverter implements Converter<Brotherhood, String> {

	@Override
	public String convert(final Brotherhood brotherhood) {
		String result;

		if (brotherhood == null)
			result = null;
		else
			result = String.valueOf(brotherhood.getId());

		return result;
	}
}
