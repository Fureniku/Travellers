package com.silvaniastudios.travellers.client.gui.toasts;

import net.minecraft.client.gui.toasts.GuiToast;
import net.minecraft.client.gui.toasts.IToast;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ScanToast implements IToast
{

    public ScanToast()
    {
    }

    @Override
    public IToast.Visibility draw(GuiToast toastGui, long delta)
    {
      return IToast.Visibility.SHOW;
    }
}