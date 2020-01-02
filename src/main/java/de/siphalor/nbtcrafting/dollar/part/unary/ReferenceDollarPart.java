package de.siphalor.nbtcrafting.dollar.part.unary;

import de.siphalor.nbtcrafting.dollar.DollarException;
import de.siphalor.nbtcrafting.dollar.DollarParser;
import de.siphalor.nbtcrafting.dollar.part.DollarPart;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

import java.util.Map;

public class ReferenceDollarPart implements DollarPart {
	private String key;

	public ReferenceDollarPart(String key) {
		this.key = key;
	}

	@Override
	public Tag evaluate(Map<String, CompoundTag> reference) throws DollarException {
		if(!reference.containsKey(key)) {
			throw new DollarException("Could not resolve reference to nbt tag '" + key + "'");
		}
		return reference.get(key);
	}

	public static class Deserializer implements DollarPart.UnaryDeserializer {
		@Override
		public boolean matches(int character, DollarParser dollarParser) {
			return Character.isJavaIdentifierStart(character);
		}

		@Override
		public DollarPart parse(DollarParser dollarParser) {
			StringBuilder stringBuilder = new StringBuilder(String.valueOf(Character.toChars(dollarParser.eat())));
			int character;
			while(true) {
				character = dollarParser.peek();
				if(Character.isJavaIdentifierPart(character)) {
					dollarParser.skip();
					stringBuilder.append(Character.toChars(character));
				} else {
					return new ReferenceDollarPart(stringBuilder.toString());
				}
			}
		}
	}
}