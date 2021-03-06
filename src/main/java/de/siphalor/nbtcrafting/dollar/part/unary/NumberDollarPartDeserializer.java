package de.siphalor.nbtcrafting.dollar.part.unary;

import de.siphalor.nbtcrafting.dollar.DollarDeserializationException;
import de.siphalor.nbtcrafting.dollar.DollarParser;
import de.siphalor.nbtcrafting.dollar.part.DollarPart;
import de.siphalor.nbtcrafting.dollar.part.ValueDollarPart;

public class NumberDollarPartDeserializer implements DollarPart.UnaryDeserializer {
	@Override
	public boolean matches(int character, DollarParser dollarParser) {
		return Character.isDigit(character);
	}

	@Override
	public DollarPart parse(DollarParser dollarParser) throws DollarDeserializationException {
		StringBuilder stringBuilder = new StringBuilder(String.valueOf(Character.toChars(dollarParser.eat())));
		boolean dot = false;
		int character;
		while (true) {
			character = dollarParser.peek();
			if (Character.isDigit(character)) {
				dollarParser.skip();
				stringBuilder.append(Character.toChars(character));
			} else if (!dot && character == '.') {
				dollarParser.skip();
				stringBuilder.append('.');
				dot = true;
			} else {
				break;
			}
		}

		try {
			if (dot)
				return ValueDollarPart.of(Double.parseDouble(stringBuilder.toString()));
			else
				return ValueDollarPart.of(Integer.parseInt(stringBuilder.toString()));
		} catch (NumberFormatException e) {
			throw new DollarDeserializationException(e);
		}
	}
}
