package com.silvaniastudios.travellers.network;

import com.silvaniastudios.travellers.capability.knowledge.KnowledgeProvider;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class KnowledgeMessage implements IMessage {

	public KnowledgeMessage() {
	}

	public int knowledge;

	public KnowledgeMessage(int knowledge) {
		this.knowledge = knowledge;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.knowledge = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.knowledge);
	}

	public static class CKnowledgeMessageHandler implements IMessageHandler<KnowledgeMessage, IMessage> {
		
		public CKnowledgeMessageHandler() {}
		
		@Override
		public IMessage onMessage(KnowledgeMessage message, MessageContext ctx) {

			IThreadListener mainThread = Minecraft.getMinecraft();

			mainThread.addScheduledTask(() -> {
				Minecraft.getMinecraft().player.getCapability(KnowledgeProvider.KNOWLEDGE, null)
						.setKnowledge(message.knowledge);
			});

			return null;
		}

	}

}
