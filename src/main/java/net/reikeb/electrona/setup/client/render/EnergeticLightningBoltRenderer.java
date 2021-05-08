package net.reikeb.electrona.setup.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;

import net.reikeb.electrona.entity.EnergeticLightningBolt;

import java.util.Random;

public class EnergeticLightningBoltRenderer extends EntityRenderer<EnergeticLightningBolt> {

    public EnergeticLightningBoltRenderer(EntityRendererManager rendererManager) {
        super(rendererManager);
    }

    public void render(EnergeticLightningBolt energeticLightningBolt, float n, float m, MatrixStack matrixStack, IRenderTypeBuffer buffer, int x) {
        float[] afloat = new float[8];
        float[] afloat1 = new float[8];
        float f = 0.0F;
        float f1 = 0.0F;
        Random random = new Random(energeticLightningBolt.seed);

        for(int i = 7; i >= 0; --i) {
            afloat[i] = f;
            afloat1[i] = f1;
            f += (float) (random.nextInt(11) - 5);
            f1 += (float) (random.nextInt(11) - 5);
        }

        IVertexBuilder ivertexbuilder = buffer.getBuffer(RenderType.lightning());
        Matrix4f matrix4f = matrixStack.last().pose();

        for(int j = 0; j < 4; ++j) {
            Random random1 = new Random(energeticLightningBolt.seed);

            for(int k = 0; k < 3; ++k) {
                int l = 7;
                int i1 = 0;
                if (k > 0) {
                    l = 7 - k;
                }

                if (k > 0) {
                    i1 = l - 2;
                }

                float f2 = afloat[l] - f;
                float f3 = afloat1[l] - f1;

                for(int j1 = l; j1 >= i1; --j1) {
                    float f4 = f2;
                    float f5 = f3;
                    if (k == 0) {
                        f2 += (float) (random1.nextInt(11) - 5);
                        f3 += (float) (random1.nextInt(11) - 5);
                    } else {
                        f2 += (float) (random1.nextInt(31) - 15);
                        f3 += (float) (random1.nextInt(31) - 15);
                    }

                    float f10 = 0.1F + (float) j * 0.2F;
                    if (k == 0) {
                        f10 = (float) ((double) f10 * ((double) j1 * 0.1D + 1.0D));
                    }

                    float f11 = 0.1F + (float)j * 0.2F;
                    if (k == 0) {
                        f11 *= (float)(j1 - 1) * 0.1F + 1.0F;
                    }

                    quad(matrix4f, ivertexbuilder, f2, f3, j1, f4, f5, 235, 175, 14, f10, f11, false, false, true, false);
                    quad(matrix4f, ivertexbuilder, f2, f3, j1, f4, f5, 235, 175, 14, f10, f11, true, false, true, true);
                    quad(matrix4f, ivertexbuilder, f2, f3, j1, f4, f5, 235, 175, 14, f10, f11, true, true, false, true);
                    quad(matrix4f, ivertexbuilder, f2, f3, j1, f4, f5, 235, 175, 14, f10, f11, false, true, false, false);
                }
            }
        }

    }

    private static void quad(Matrix4f p_229116_0_, IVertexBuilder p_229116_1_, float p_229116_2_, float p_229116_3_, int p_229116_4_, float p_229116_5_, float p_229116_6_, int p_229116_7_, int p_229116_8_, int p_229116_9_, float p_229116_10_, float p_229116_11_, boolean p_229116_12_, boolean p_229116_13_, boolean p_229116_14_, boolean p_229116_15_) {
        p_229116_1_.vertex(p_229116_0_, p_229116_2_ + (p_229116_12_ ? p_229116_11_ : -p_229116_11_), (float) (p_229116_4_ * 16), p_229116_3_ + (p_229116_13_ ? p_229116_11_ : -p_229116_11_)).color(p_229116_7_, p_229116_8_, p_229116_9_, 128).endVertex();
        p_229116_1_.vertex(p_229116_0_, p_229116_5_ + (p_229116_12_ ? p_229116_10_ : -p_229116_10_), (float) ((p_229116_4_ + 1) * 16), p_229116_6_ + (p_229116_13_ ? p_229116_10_ : -p_229116_10_)).color(p_229116_7_, p_229116_8_, p_229116_9_, 128).endVertex();
        p_229116_1_.vertex(p_229116_0_, p_229116_5_ + (p_229116_14_ ? p_229116_10_ : -p_229116_10_), (float) ((p_229116_4_ + 1) * 16), p_229116_6_ + (p_229116_15_ ? p_229116_10_ : -p_229116_10_)).color(p_229116_7_, p_229116_8_, p_229116_9_, 128).endVertex();
        p_229116_1_.vertex(p_229116_0_, p_229116_2_ + (p_229116_14_ ? p_229116_11_ : -p_229116_11_), (float) (p_229116_4_ * 16), p_229116_3_ + (p_229116_15_ ? p_229116_11_ : -p_229116_11_)).color(p_229116_7_, p_229116_8_, p_229116_9_, 128).endVertex();
    }

    public ResourceLocation getTextureLocation(EnergeticLightningBolt energeticLightningBolt) {
        return AtlasTexture.LOCATION_BLOCKS;
    }
}
