/**
 * 
 */
package com.silvaniastudios.travellers;

import com.silvaniastudios.travellers.capability.schematicData.ISchematicData;
import com.silvaniastudios.travellers.capability.schematicData.SchematicDataProvider;

import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

/**
 * @author jamesm2w
 */
public class ChatHandler {
	
	public static Style style (TextFormatting colour) {
		Style style = new Style();
		style.setColor(colour);
		
		return style;
	}
	
	public static ITextComponent schematicString (ItemStack schematicStack) {
		
		if (schematicStack.hasCapability(SchematicDataProvider.SCHEMATIC_DATA, null)) {
			ISchematicData schemData = schematicStack.getCapability(SchematicDataProvider.SCHEMATIC_DATA, null);
			
			TextComponentTranslation message = new TextComponentTranslation(schematicStack.getDisplayName());
			message.setStyle(style(schemData.getRarity().toMCRarity().rarityColor));

			return message;
		}
		
		return new TextComponentTranslation(schematicStack.getDisplayName());
	}
	
	public static ITextComponent translatedString (String translation, TextFormatting colour) {
		TextComponentTranslation translated = new TextComponentTranslation(translation);
		translated.setStyle(style(colour));
		
		return translated;
	}
	
	public static ITextComponent translatedString (String translation, TextFormatting colour, Object... args) {
		TextComponentTranslation translated = new TextComponentTranslation(translation, args);
		translated.setStyle(style(colour));
		
		return translated;
	}
	
	public static ITextComponent number (int number, TextFormatting colour) {
		TextComponentString msg = new TextComponentString(String.valueOf(number));
		msg.setStyle(style(colour));
		return msg;
	}
}
