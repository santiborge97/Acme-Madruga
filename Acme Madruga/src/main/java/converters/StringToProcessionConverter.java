
package converters;

import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import repositories.ProcessionRepository;
import domain.Procession;

@Component
@Transactional
public class StringToProcessionConverter implements Converter<String, Procession> {

	@Autowired
	private ProcessionRepository	processionRepository;


	@Override
	public Procession convert(final String text) {
		Procession result;
		final int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.processionRepository.findOne(id);
			}
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}
}
