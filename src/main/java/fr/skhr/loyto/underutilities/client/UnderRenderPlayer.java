package fr.skhr.loyto.UnderUtilities.client;

import java.util.UUID;

import org.lwjgl.opengl.GL11;

import com.mojang.authlib.GameProfile;

import cpw.mods.fml.common.eventhandler.Event;
import fr.skhr.loyto.UnderUtilities.proxy.ClientProxy;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.tileentity.TileEntitySkullRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.common.MinecraftForge;

public class UnderRenderPlayer extends RenderPlayer {
    private static final ResourceLocation defaultSkin = new ResourceLocation("textures/entity/steve.png");
    private ResourceLocation locationSkin;
    
    public UnderRenderPlayer() {
        this.setRenderManager(RenderManager.instance);
    }
    
    protected ResourceLocation getEntityTexture(final AbstractClientPlayer player) {
        this.getResourceLocationSkinAndCape(player.getCommandSenderName());
        if(Minecraft.getMinecraft().getTextureManager().getTexture(ClientProxy.getSkinLocation(player.getCommandSenderName())).getClass().getName().equalsIgnoreCase(DynamicTexture.class.getName())) {
        	loadPlayerSkinDT(this.getLocationSkin(), player.getCommandSenderName());
        }
        else {
        	loadPlayerSkinST(this.getLocationSkin(), player.getCommandSenderName());
        }
        
        return this.getLocationSkin();
    }
    
    protected ResourceLocation getEntityTexture(final Entity entity) {
        return this.getEntityTexture((AbstractClientPlayer)entity);
    }
    
    public void bindTexture(final ResourceLocation location) {
        if (location == null) {
            return;
        }
        final TextureManager texturemanager = Minecraft.getMinecraft().getTextureManager();
        try {
            texturemanager.bindTexture(location);
        }
        catch (ReportedException ex) {}
        catch (NullPointerException ex2) {}
    }
    
    public void renderFirstPersonArm(final EntityPlayer player) {
        bindTexture(getResourceLocationSkinAndCape(player.getCommandSenderName()));
        super.renderFirstPersonArm(player);
    }
    
    protected void renderEquippedItems(final AbstractClientPlayer player, final float p_77029_2_) {
        final RenderPlayerEvent.Specials.Pre event = new RenderPlayerEvent.Specials.Pre((EntityPlayer)player, (RenderPlayer)this, p_77029_2_);
        if (MinecraftForge.EVENT_BUS.post((Event)event)) {
            return;
        }
        GL11.glColor3f(1.0f, 1.0f, 1.0f);
        super.renderEquippedItems(player, p_77029_2_);
        super.renderArrowsStuckInEntity((EntityLivingBase)player, p_77029_2_);
        final ItemStack itemstack = player.inventory.armorItemInSlot(3);
        if (itemstack != null && event.renderHelmet) {
            GL11.glPushMatrix();
            this.modelBipedMain.bipedHead.postRender(0.0625f);
            if (itemstack.getItem() instanceof ItemBlock) {
                final IItemRenderer customRenderer = MinecraftForgeClient.getItemRenderer(itemstack, IItemRenderer.ItemRenderType.EQUIPPED);
                final boolean is3D = customRenderer != null && customRenderer.shouldUseRenderHelper(IItemRenderer.ItemRenderType.EQUIPPED, itemstack, IItemRenderer.ItemRendererHelper.BLOCK_3D);
                if (is3D || RenderBlocks.renderItemIn3d(Block.getBlockFromItem(itemstack.getItem()).getRenderType())) {
                    final float f1 = 0.625f;
                    GL11.glTranslatef(0.0f, -0.25f, 0.0f);
                    GL11.glRotatef(90.0f, 0.0f, 1.0f, 0.0f);
                    GL11.glScalef(f1, -f1, -f1);
                }
                this.renderManager.itemRenderer.renderItem((EntityLivingBase)player, itemstack, 0);
            }
            else if (itemstack.getItem() == Items.skull) {
                final float f1 = 1.0625f;
                GL11.glScalef(f1, -f1, -f1);
                GameProfile gameprofile = null;
                if (itemstack.hasTagCompound()) {
                    final NBTTagCompound nbttagcompound = itemstack.getTagCompound();
                    if (nbttagcompound.hasKey("SkullOwner", 10)) {
                        gameprofile = NBTUtil.func_152459_a(nbttagcompound.getCompoundTag("SkullOwner"));
                    }
                    else if (nbttagcompound.hasKey("SkullOwner", 8) && !StringUtils.isNullOrEmpty(nbttagcompound.getString("SkullOwner"))) {
                        gameprofile = new GameProfile((UUID)null, nbttagcompound.getString("SkullOwner"));
                    }
                }
                TileEntitySkullRenderer.field_147536_b.func_152674_a(-0.5f, 0.0f, -0.5f, 1, 180.0f, itemstack.getItemDamage(), gameprofile);
            }
            GL11.glPopMatrix();
        }
        if (player.getCommandSenderName().equals("deadmaus") && this.hasSkin()) {
            bindTexture(getLocationSkin());
            for (int j = 0; j < 2; ++j) {
                final float f2 = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * p_77029_2_ - (player.prevRenderYawOffset + (player.renderYawOffset - player.prevRenderYawOffset) * p_77029_2_);
                final float f3 = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * p_77029_2_;
                GL11.glPushMatrix();
                GL11.glRotatef(f2, 0.0f, 1.0f, 0.0f);
                GL11.glRotatef(f3, 1.0f, 0.0f, 0.0f);
                GL11.glTranslatef(0.375f * (j * 2 - 1), 0.0f, 0.0f);
                GL11.glTranslatef(0.0f, -0.375f, 0.0f);
                GL11.glRotatef(-f3, 1.0f, 0.0f, 0.0f);
                GL11.glRotatef(-f2, 0.0f, 1.0f, 0.0f);
                final float f4 = 1.3333334f;
                GL11.glScalef(f4, f4, f4);
                this.modelBipedMain.renderEars(0.0625f);
                GL11.glPopMatrix();
            }
        }
        boolean flag = false;
        flag = (event.renderCape && flag);
        if (flag && !player.isInvisible() && !player.getHideCape()) {
            bindTexture(player.getLocationCape());
            GL11.glPushMatrix();
            GL11.glTranslatef(0.0f, 0.0f, 0.125f);
            final double d3 = player.field_71091_bM + (player.field_71094_bP - player.field_71091_bM) * p_77029_2_ - (player.prevPosX + (player.posX - player.prevPosX) * p_77029_2_);
            final double d4 = player.field_71096_bN + (player.field_71095_bQ - player.field_71096_bN) * p_77029_2_ - (player.prevPosY + (player.posY - player.prevPosY) * p_77029_2_);
            final double d5 = player.field_71097_bO + (player.field_71085_bR - player.field_71097_bO) * p_77029_2_ - (player.prevPosZ + (player.posZ - player.prevPosZ) * p_77029_2_);
            final float f5 = player.prevRenderYawOffset + (player.renderYawOffset - player.prevRenderYawOffset) * p_77029_2_;
            final double d6 = MathHelper.sin(f5 * 3.1415927f / 180.0f);
            final double d7 = -MathHelper.cos(f5 * 3.1415927f / 180.0f);
            float f6 = (float)d4 * 10.0f;
            if (f6 < -6.0f) {
                f6 = -6.0f;
            }
            if (f6 > 32.0f) {
                f6 = 32.0f;
            }
            float f7 = (float)(d3 * d6 + d5 * d7) * 100.0f;
            final float f8 = (float)(d3 * d7 - d5 * d6) * 100.0f;
            if (f7 < 0.0f) {
                f7 = 0.0f;
            }
            final float f9 = player.prevCameraYaw + (player.cameraYaw - player.prevCameraYaw) * p_77029_2_;
            f6 += MathHelper.sin((player.prevDistanceWalkedModified + (player.distanceWalkedModified - player.prevDistanceWalkedModified) * p_77029_2_) * 6.0f) * 32.0f * f9;
            if (player.isSneaking()) {
                f6 += 25.0f;
            }
            GL11.glRotatef(6.0f + f7 / 2.0f + f6, 1.0f, 0.0f, 0.0f);
            GL11.glRotatef(f8 / 2.0f, 0.0f, 0.0f, 1.0f);
            GL11.glRotatef(-f8 / 2.0f, 0.0f, 1.0f, 0.0f);
            GL11.glRotatef(180.0f, 0.0f, 1.0f, 0.0f);
            this.modelBipedMain.renderCloak(0.0625f);
            GL11.glPopMatrix();
        }
        ItemStack itemstack2 = player.inventory.getCurrentItem();
        if (itemstack2 != null && event.renderItem) {
            GL11.glPushMatrix();
            this.modelBipedMain.bipedRightArm.postRender(0.0625f);
            GL11.glTranslatef(-0.0625f, 0.4375f, 0.0625f);
            if (player.fishEntity != null) {
                itemstack2 = new ItemStack(Items.stick);
            }
            EnumAction enumaction = null;
            if (player.getItemInUseCount() > 0) {
                enumaction = itemstack2.getItemUseAction();
            }
            final IItemRenderer customRenderer2 = MinecraftForgeClient.getItemRenderer(itemstack2, IItemRenderer.ItemRenderType.EQUIPPED);
            final boolean is3D2 = customRenderer2 != null && customRenderer2.shouldUseRenderHelper(IItemRenderer.ItemRenderType.EQUIPPED, itemstack2, IItemRenderer.ItemRendererHelper.BLOCK_3D);
            if (is3D2 || (itemstack2.getItem() instanceof ItemBlock && RenderBlocks.renderItemIn3d(Block.getBlockFromItem(itemstack2.getItem()).getRenderType()))) {
                float f4 = 0.5f;
                GL11.glTranslatef(0.0f, 0.1875f, -0.3125f);
                f4 *= 0.75f;
                GL11.glRotatef(20.0f, 1.0f, 0.0f, 0.0f);
                GL11.glRotatef(45.0f, 0.0f, 1.0f, 0.0f);
                GL11.glScalef(-f4, -f4, f4);
            }
            else if (itemstack2.getItem() == Items.bow) {
                final float f4 = 0.625f;
                GL11.glTranslatef(0.0f, 0.125f, 0.3125f);
                GL11.glRotatef(-20.0f, 0.0f, 1.0f, 0.0f);
                GL11.glScalef(f4, -f4, f4);
                GL11.glRotatef(-100.0f, 1.0f, 0.0f, 0.0f);
                GL11.glRotatef(45.0f, 0.0f, 1.0f, 0.0f);
            }
            else if (itemstack2.getItem().isFull3D()) {
                final float f4 = 0.625f;
                if (itemstack2.getItem().shouldRotateAroundWhenRendering()) {
                    GL11.glRotatef(180.0f, 0.0f, 0.0f, 1.0f);
                    GL11.glTranslatef(0.0f, -0.125f, 0.0f);
                }
                if (player.getItemInUseCount() > 0 && enumaction == EnumAction.block) {
                    GL11.glTranslatef(0.05f, 0.0f, -0.1f);
                    GL11.glRotatef(-50.0f, 0.0f, 1.0f, 0.0f);
                    GL11.glRotatef(-10.0f, 1.0f, 0.0f, 0.0f);
                    GL11.glRotatef(-60.0f, 0.0f, 0.0f, 1.0f);
                }
                GL11.glTranslatef(0.0f, 0.1875f, 0.0f);
                GL11.glScalef(f4, -f4, f4);
                GL11.glRotatef(-100.0f, 1.0f, 0.0f, 0.0f);
                GL11.glRotatef(45.0f, 0.0f, 1.0f, 0.0f);
            }
            else {
                final float f4 = 0.375f;
                GL11.glTranslatef(0.25f, 0.1875f, -0.1875f);
                GL11.glScalef(f4, f4, f4);
                GL11.glRotatef(60.0f, 0.0f, 0.0f, 1.0f);
                GL11.glRotatef(-90.0f, 1.0f, 0.0f, 0.0f);
                GL11.glRotatef(20.0f, 0.0f, 0.0f, 1.0f);
            }
            if (itemstack2.getItem().requiresMultipleRenderPasses()) {
                for (int k = 0; k < itemstack2.getItem().getRenderPasses(itemstack.getItemDamage()); ++k) {
                    final int i = itemstack2.getItem().getColorFromItemStack(itemstack2, k);
                    final float f10 = (i >> 16 & 0xFF) / 255.0f;
                    final float f11 = (i >> 8 & 0xFF) / 255.0f;
                    final float f5 = (i & 0xFF) / 255.0f;
                    GL11.glColor4f(f10, f11, f5, 1.0f);
                    this.renderManager.itemRenderer.renderItem((EntityLivingBase)player, itemstack2, k);
                }
            }
            else {
                final int k = itemstack2.getItem().getColorFromItemStack(itemstack2, 0);
                final float f12 = (k >> 16 & 0xFF) / 255.0f;
                final float f10 = (k >> 8 & 0xFF) / 255.0f;
                final float f11 = (k & 0xFF) / 255.0f;
                GL11.glColor4f(f12, f10, f11, 1.0f);
                this.renderManager.itemRenderer.renderItem((EntityLivingBase)player, itemstack2, 0);
            }
            GL11.glPopMatrix();
        }
        MinecraftForge.EVENT_BUS.post((Event)new RenderPlayerEvent.Specials.Post((EntityPlayer)player, (RenderPlayer)this, p_77029_2_));
    }
    
    public boolean hasSkin() {
        return this.locationSkin != null;
    }
    
    public ResourceLocation getResourceLocationSkinAndCape(final String username) {
    	return this.locationSkin = ClientProxy.getSkinLocation(username);
    }
    
    public ResourceLocation getLocationSkin() {
    	return (this.locationSkin == null) ? UnderRenderPlayer.defaultSkin : this.locationSkin;
    }
    
    public static DynamicTexture loadPlayerSkinDT(final ResourceLocation resourceLocationIn, final String username) {
    	return (DynamicTexture)Minecraft.getMinecraft().getTextureManager().getTexture(ClientProxy.getSkinLocation(username));
    }
    
    public static SimpleTexture loadPlayerSkinST(final ResourceLocation resourceLocationIn, final String username) {
    	return (SimpleTexture)Minecraft.getMinecraft().getTextureManager().getTexture(ClientProxy.getSkinLocation(username));
    }
}
