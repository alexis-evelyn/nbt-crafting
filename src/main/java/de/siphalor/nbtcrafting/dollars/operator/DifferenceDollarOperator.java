package de.siphalor.nbtcrafting.dollars.operator;

import de.siphalor.nbtcrafting.dollars.DollarException;
import de.siphalor.nbtcrafting.dollars.DollarParser;
import de.siphalor.nbtcrafting.dollars.DollarPart;
import de.siphalor.nbtcrafting.util.NbtHelper;
import net.minecraft.nbt.AbstractNumberTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;

import java.io.IOException;

public class DifferenceDollarOperator extends BinaryDollarOperator {
	public DifferenceDollarOperator(DollarPart first, DollarPart second) {
		super(first, second);
	}

	@Override
	public Tag apply(Tag first, Tag second) {
		if(first instanceof AbstractNumberTag && second instanceof AbstractNumberTag)
			return DoubleTag.of(((AbstractNumberTag) first).getDouble() - ((AbstractNumberTag) second).getDouble());
		else
			return StringTag.of(NbtHelper.asString(first).replace(NbtHelper.asString(second), ""));
	}

	public static class Deserializer implements DollarPart.Deserializer {
		@Override
		public boolean matches(int character, DollarParser dollarParser, boolean hasOtherPart) {
			return hasOtherPart && character == '-';
		}

		@Override
		public DollarPart parse(DollarParser dollarParser, DollarPart lastDollarPart, int priority) throws DollarException, IOException {
			dollarParser.skip();
			return new DifferenceDollarOperator(lastDollarPart, dollarParser.parse(priority));
		}
	}
}