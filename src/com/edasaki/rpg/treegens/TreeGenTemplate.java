package com.edasaki.rpg.treegens;

import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

import net.minecraft.server.v1_10_R1.Block;
import net.minecraft.server.v1_10_R1.BlockLeaves;
import net.minecraft.server.v1_10_R1.BlockLogAbstract;
import net.minecraft.server.v1_10_R1.BlockPosition;
import net.minecraft.server.v1_10_R1.Blocks;
import net.minecraft.server.v1_10_R1.IBlockData;
import net.minecraft.server.v1_10_R1.Material;
import net.minecraft.server.v1_10_R1.MathHelper;
import net.minecraft.server.v1_10_R1.World;
import net.minecraft.server.v1_10_R1.WorldGenTreeAbstract;

public class TreeGenTemplate extends WorldGenTreeAbstract {

    private Random rand;
    private World worldObj;

    static class Position extends BlockPosition {
        private final int c;

        public Position(BlockPosition paramBlockPosition, int paramInt) {
            super(paramBlockPosition.getX(), paramBlockPosition.getY(), paramBlockPosition.getZ());
            this.c = paramInt;
        }

        public int s() {
            return this.c;
        }
    }

    private BlockPosition basePos = BlockPosition.ZERO;

    int heightLimit;
    int height;
    double heightAttenuation = 0.618D;
    double branchSlope = 0.381D;
    double scaleWidth = 1.0D;
    double leafDensity = 1.0D;
    int trunkSize = 1;
    int heightLimitLimit = 12;
    int leafDistanceLimit = 4;
    List<Position> leafNodes;

    public TreeGenTemplate(boolean paramBoolean) {
        super(paramBoolean);
    }

    /**
     * Generates a list of leaf nodes for the tree, to be populated by generateLeaves.
     */
    void generateLeafNodeList() {
        this.height = ((int) (this.heightLimit * this.heightAttenuation));
        if (this.height >= this.heightLimit) {
            this.height = (this.heightLimit - 1);
        }

        int n = (int) (1.382D + Math.pow(this.leafDensity * this.heightLimit / 13.0D, 2.0D));
        if (n < 1) {
            n = 1;
        }

        int i1 = this.basePos.getY() + this.height;
        int i2 = this.heightLimit - this.leafDistanceLimit;

        this.leafNodes = Lists.newArrayList();
        this.leafNodes.add(new Position(this.basePos.up(i2), i1));
        for (; i2 >= 0; i2--) {
            float f1 = layerSize(i2);
            if (f1 >= 0.0F) {

                for (int i3 = 0; i3 < n; i3++) {
                    double d1 = this.scaleWidth * f1 * (this.rand.nextFloat() + 0.328D);
                    double d2 = this.rand.nextFloat() * 2.0F * 3.141592653589793D;

                    double d3 = d1 * Math.sin(d2) + 0.5D;
                    double d4 = d1 * Math.cos(d2) + 0.5D;

                    BlockPosition localBlockPosition1 = this.basePos.a(d3, i2 - 1, d4);
                    BlockPosition localBlockPosition2 = localBlockPosition1.up(this.leafDistanceLimit);

                    if (checkBlockLine(localBlockPosition1, localBlockPosition2) == -1) {
                        int i4 = this.basePos.getX() - localBlockPosition1.getX();
                        int i5 = this.basePos.getZ() - localBlockPosition1.getZ();

                        double d5 = localBlockPosition1.getY() - Math.sqrt(i4 * i4 + i5 * i5) * this.branchSlope;
                        int i6 = d5 > i1 ? i1 : (int) d5;
                        BlockPosition localBlockPosition3 = new BlockPosition(this.basePos.getX(), i6, this.basePos.getZ());

                        if (checkBlockLine(localBlockPosition3, localBlockPosition1) == -1) {
                            this.leafNodes.add(new Position(localBlockPosition1, localBlockPosition3.getY()));
                        }
                    }
                }
            }
        }
    }

    void genTreeLayer(BlockPosition paramBlockPosition, float paramFloat, IBlockData paramIBlockData) {
        int n = (int) (paramFloat + 0.618D);

        for (int i1 = -n; i1 <= n; i1++) {
            for (int i2 = -n; i2 <= n; i2++) {
                if (Math.pow(Math.abs(i1) + 0.5D, 2.0D) + Math.pow(Math.abs(i2) + 0.5D, 2.0D) <= paramFloat * paramFloat) {
                    BlockPosition localBlockPosition = paramBlockPosition.a(i1, 0, i2);

                    Material localMaterial = this.worldObj.getType(localBlockPosition).getMaterial();
                    if ((localMaterial == Material.AIR) || (localMaterial == Material.LEAVES)) {
                        a(this.worldObj, localBlockPosition, paramIBlockData);
                    }
                }
            }
        }
    }

    /**
     * Gets the rough size of a layer of the tree.
     */
    float layerSize(int paramInt) {
        if (paramInt < this.heightLimit * 0.3F) {
            return -1.0F;
        }

        float f1 = this.heightLimit / 2.0F;
        float f2 = f1 - paramInt;

        float f3 = MathHelper.c(f1 * f1 - f2 * f2);
        if (f2 == 0.0F) {
            f3 = f1;
        } else if (Math.abs(f2) >= f1) {
            return 0.0F;
        }

        return f3 * 0.5F;
    }

    float leafSize(int paramInt) {
        if ((paramInt < 0) || (paramInt >= this.leafDistanceLimit)) {
            return -1.0F;
        }
        if ((paramInt == 0) || (paramInt == this.leafDistanceLimit - 1)) {
            return 2.0F;
        }

        return 3.0F;
    }

    /**
     * Generates the leaves surrounding an individual entry in the leafNodes list.
     */
    void generateLeafNode(BlockPosition paramBlockPosition) {
        for (int n = 0; n < this.leafDistanceLimit; n++) {
            genTreeLayer(paramBlockPosition.up(n), leafSize(n), Blocks.LEAVES.getBlockData().set(BlockLeaves.CHECK_DECAY, Boolean.valueOf(false)));
        }
    }

    void placeBlockLine(BlockPosition paramBlockPosition1, BlockPosition paramBlockPosition2, Block paramBlock) {
        BlockPosition localBlockPosition1 = paramBlockPosition2.a(-paramBlockPosition1.getX(), -paramBlockPosition1.getY(), -paramBlockPosition1.getZ());

        int n = b(localBlockPosition1);

        float f1 = localBlockPosition1.getX() / n;
        float f2 = localBlockPosition1.getY() / n;
        float f3 = localBlockPosition1.getZ() / n;

        for (int i1 = 0; i1 <= n; i1++) {
            BlockPosition localBlockPosition2 = paramBlockPosition1.a(0.5F + i1 * f1, 0.5F + i1 * f2, 0.5F + i1 * f3);
            BlockLogAbstract.EnumLogRotation localEnumLogRotation = b(paramBlockPosition1, localBlockPosition2);

            a(this.worldObj, localBlockPosition2, paramBlock.getBlockData().set(BlockLogAbstract.AXIS, localEnumLogRotation));
        }
    }

    private int b(BlockPosition paramBlockPosition) {
        int n = MathHelper.a(paramBlockPosition.getX());
        int i1 = MathHelper.a(paramBlockPosition.getY());
        int i2 = MathHelper.a(paramBlockPosition.getZ());

        if ((i2 > n) && (i2 > i1))
            return i2;
        if (i1 > n) {
            return i1;
        }

        return n;
    }

    private BlockLogAbstract.EnumLogRotation b(BlockPosition paramBlockPosition1, BlockPosition paramBlockPosition2) {
        BlockLogAbstract.EnumLogRotation localEnumLogRotation = BlockLogAbstract.EnumLogRotation.Y;
        int n = Math.abs(paramBlockPosition2.getX() - paramBlockPosition1.getX());
        int i1 = Math.abs(paramBlockPosition2.getZ() - paramBlockPosition1.getZ());
        int i2 = Math.max(n, i1);

        if (i2 > 0) {
            if (n == i2) {
                localEnumLogRotation = BlockLogAbstract.EnumLogRotation.X;
            } else if (i1 == i2) {
                localEnumLogRotation = BlockLogAbstract.EnumLogRotation.Z;
            }
        }
        return localEnumLogRotation;
    }

    /**
     * Generates the leaf portion of the tree as specified by the leafNodes list.
     */
    void generateLeaves() {
        for (Position localPosition : this.leafNodes) {
            generateLeafNode(localPosition);
        }
    }

    /**
     * Indicates whether or not a leaf node requires additional wood to be added to preserve integrity.
     */
    boolean leafNodeNeedsBase(int paramInt) {
        return paramInt >= this.heightLimit * 0.2D;
    }

    /**
     * Places the trunk for the big tree that is being generated. Able to generate double-sized trunks by changing a
     * field that is always 1 to 2.
     */
    void generateTrunk() {
        BlockPosition localBlockPosition1 = this.basePos;
        BlockPosition localBlockPosition2 = this.basePos.up(this.height);
        Block localBlock = Blocks.LOG;

        placeBlockLine(localBlockPosition1, localBlockPosition2, localBlock);
        if (this.trunkSize == 2) {
            placeBlockLine(localBlockPosition1.east(), localBlockPosition2.east(), localBlock);
            placeBlockLine(localBlockPosition1.east().south(), localBlockPosition2.east().south(), localBlock);
            placeBlockLine(localBlockPosition1.south(), localBlockPosition2.south(), localBlock);
        }
    }

    /**
     * Generates additional wood blocks to fill out the bases of different leaf nodes that would otherwise degrade.
     */
    void generateLeafNodeBases() {
        for (Position localPosition : this.leafNodes) {
            int n = localPosition.s();
            BlockPosition localBlockPosition = new BlockPosition(this.basePos.getX(), n, this.basePos.getZ());

            if (!localBlockPosition.equals(localPosition) && leafNodeNeedsBase(n - this.basePos.getY())) {
                placeBlockLine(localBlockPosition, localPosition, Blocks.LOG);
            }
        }
    }

    /**
     * Checks a line of blocks in the world from the first coordinate to triplet to the second,
     * returning the distance (in blocks) before a non-air, non-leaf block is encountered and/or the
     * end is encountered.
     */
    int checkBlockLine(BlockPosition paramBlockPosition1, BlockPosition paramBlockPosition2) {
        BlockPosition localBlockPosition1 = paramBlockPosition2.a(-paramBlockPosition1.getX(), -paramBlockPosition1.getY(), -paramBlockPosition1.getZ());

        int n = b(localBlockPosition1);

        float f1 = localBlockPosition1.getX() / n;
        float f2 = localBlockPosition1.getY() / n;
        float f3 = localBlockPosition1.getZ() / n;

        if (n == 0) {
            return -1;
        }

        for (int i1 = 0; i1 <= n; i1++) {
            BlockPosition localBlockPosition2 = paramBlockPosition1.a(0.5F + i1 * f1, 0.5F + i1 * f2, 0.5F + i1 * f3);

            if (!a(this.worldObj.getType(localBlockPosition2).getBlock())) {
                return i1;
            }
        }

        return -1;
    }

    public boolean generate(World paramWorld, Random paramRandom, BlockPosition paramBlockPosition) {
        this.worldObj = paramWorld;
        this.basePos = paramBlockPosition;

        this.rand = new Random(paramRandom.nextLong());

        if (this.heightLimit == 0) {
            this.heightLimit = (5 + this.rand.nextInt(this.heightLimitLimit));
        }

        if (!validTreeLocation()) {
            return false;
        }

        generateLeafNodeList();
        generateLeaves();
        generateTrunk();
        generateLeafNodeBases();

        return true;
    }

    /**
     * Returns a boolean indicating whether or not the current location for the tree, spanning
     * basePos to to the height limit, is valid.
     */
    private boolean validTreeLocation() {
        Block localBlock = this.worldObj.getType(this.basePos.down()).getBlock();
        if ((localBlock != Blocks.DIRT) && (localBlock != Blocks.GRASS) && (localBlock != Blocks.FARMLAND)) {
            return false;
        }

        int n = checkBlockLine(this.basePos, this.basePos.up(this.heightLimit - 1));

        if (n == -1) {
            return true;
        }

        if (n < 6) {
            return false;
        }

        this.heightLimit = n;
        return true;
    }

}
