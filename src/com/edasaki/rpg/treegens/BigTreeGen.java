package com.edasaki.rpg.treegens;

import java.util.ArrayList;
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

public class BigTreeGen extends WorldGenTreeAbstract {

    private Random rand;
    private World worldObj;

    static class Position extends BlockPosition {
        private final int posHeight;

        public Position(BlockPosition posBlock, int posHeight) {
            super(posBlock.getX(), posBlock.getY(), posBlock.getZ());
            //            this.posHeight = posHeight;
            this.posHeight = posBlock.getY();
        }

        public int height() {
            return this.posHeight;
        }
    }

    private BlockPosition basePos = BlockPosition.ZERO;

    int heightLimit;
    int height;
    double leafSegmentHeight = 0.618D;
    double branchSlope = 0.381D;
    double scaleWidth = 1.0D;
    double leafDensity = 1.25D;
    int trunkSize = 2;
    int leafBlobSize = 4;
    List<Position> leafNodes;

    public BigTreeGen(boolean paramBoolean) {
        super(paramBoolean);
    }

    /**
     * Generates a list of leaf nodes for the tree, to be populated by generateLeaves.
     */
    void generateLeafNodeList() {
        this.height = ((int) (this.heightLimit * this.leafSegmentHeight));
        if (this.height >= this.heightLimit) {
            this.height = (this.heightLimit - 1);
        }

        int leafNodeCount = (int) (1 + Math.pow(this.leafDensity * this.heightLimit / 13.0D, 2.0D));
        if (leafNodeCount < 1) {
            leafNodeCount = 1;
        }

        int leafLowestLayer = this.basePos.getY() + this.height;
        int leafLayerHeight = this.heightLimit - this.leafBlobSize;

        this.leafNodes = Lists.newArrayList();
        this.leafNodes.add(new Position(this.basePos.up(leafLayerHeight), leafLowestLayer));
        for (; leafLayerHeight >= 0; leafLayerHeight--) {
            float layerSize = layerSize(leafLayerHeight);
            if (layerSize >= 0.0F) {
                for (int k = 0; k < leafNodeCount; k++) {
                    double radius = this.scaleWidth * layerSize * (this.rand.nextFloat() + 0.328D);
                    double angle = this.rand.nextFloat() * 2.0F * 3.141592653589793D;

                    double xOffset = radius * Math.sin(angle) + 0.5D;
                    double zOffset = radius * Math.cos(angle) + 0.5D;

                    BlockPosition leafBlobCenter = this.basePos.a(xOffset, leafLayerHeight - 1, zOffset);
                    BlockPosition leafBlobTop = leafBlobCenter.up(this.leafBlobSize);

                    if (checkBlockLine(leafBlobCenter, leafBlobTop) == -1) {
                        int xDiff = this.basePos.getX() - leafBlobCenter.getX();
                        int zDiff = this.basePos.getZ() - leafBlobCenter.getZ();

                        double botY = leafBlobCenter.getY() - Math.sqrt(xDiff * xDiff + zDiff * zDiff) * this.branchSlope;
                        int i6 = botY > leafLowestLayer ? leafLowestLayer : (int) botY;
                        BlockPosition leafBlobBot = new BlockPosition(this.basePos.getX(), i6, this.basePos.getZ());

                        if (checkBlockLine(leafBlobBot, leafBlobCenter) == -1) {
                            this.leafNodes.add(new Position(leafBlobCenter, leafBlobBot.getY()));
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
        if (paramInt < this.heightLimit * (0.35F + Math.random() * 0.30F)) {
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

    private float leafSize(int leafBlobLayer) {
        if ((leafBlobLayer < 0) || (leafBlobLayer >= this.leafBlobSize)) {
            return -1.0f;
        }
        if ((leafBlobLayer == 0) || (leafBlobLayer == this.leafBlobSize - 1)) {
            return 2.0f;
        }
        return 3.0f;
    }

    /**
     * Generates the leaves surrounding an individual entry in the leafNodes list.
     */
    void generateLeafNode(BlockPosition nodeCenter) {
        for (int currLayer = 0; currLayer < this.leafBlobSize; currLayer++) {
            genTreeLayer(nodeCenter.up(currLayer), leafSize(currLayer), Blocks.LEAVES.getBlockData().set(BlockLeaves.CHECK_DECAY, Boolean.valueOf(false)));
        }
    }

    void placeBlockLine(BlockPosition paramBlockPosition1, BlockPosition paramBlockPosition2, Block paramBlock) {
        BlockPosition localBlockPosition1 = paramBlockPosition2.a(-paramBlockPosition1.getX(), -paramBlockPosition1.getY(), -paramBlockPosition1.getZ());

        int n = b(localBlockPosition1);

        if (n == 0)
            return;

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
        int i1 = MathHelper.a(paramBlockPosition.getX());
        int n = MathHelper.a(paramBlockPosition.getY());
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
     * Places the trunk for the big tree that is being generated. Able to generate double-sized trunks by changing a
     * field that is always 1 to 2.
     */
    void generateTrunk() {
        BlockPosition trunkBase = this.basePos;
        BlockPosition trunkTop = this.basePos.up(this.height);
        Block trunkBlock = Blocks.LOG;

        placeBlockLine(trunkBase, trunkTop, trunkBlock);
        if (this.trunkSize == 2) {
            placeBlockLine(trunkBase.east(), trunkTop.east(), trunkBlock);
            placeBlockLine(trunkBase.east().south(), trunkTop.east().south(), trunkBlock);
            placeBlockLine(trunkBase.south(), trunkTop.south(), trunkBlock);
        }
    }

    void generateTrunkBase() {
        if (trunkSize == 2) {
            BlockPosition trunkBase = this.basePos;
            /*
             * xxxx 
             * xb.x
             * x..x
             * xxxx
             */
            BlockPosition[] arr = {
                    trunkBase.north().west(),
                    trunkBase.north(),
                    trunkBase.north().east(),
                    trunkBase.north().east().east(),
                    trunkBase.west(),
                    trunkBase.east().east(),
                    trunkBase.south().west(),
                    trunkBase.south().east().east(),
                    trunkBase.south().south().west(),
                    trunkBase.south().south(),
                    trunkBase.south().south().east(),
                    trunkBase.south().south().east().east()
            };
            for (BlockPosition temp : arr) {
                if (Math.random() < 0.6)
                    placeBlockLine(temp, temp.up((int) (Math.random() * 4) + 1), Blocks.LOG);
            }
        }
        if (trunkSize == 1) {
            BlockPosition trunkBase = this.basePos;
            /*
             * xxx 
             * xbx
             * xxx
             */
            BlockPosition[] arr = {
                    trunkBase.north().west(),
                    trunkBase.north(),
                    trunkBase.north().east(),
                    trunkBase.west(),
                    trunkBase.east(),
                    trunkBase.south().west(),
                    trunkBase.south(),
                    trunkBase.south().east(),
            };
            for (BlockPosition temp : arr) {
                if (Math.random() < 0.25)
                    placeBlockLine(temp, temp.up((int) (Math.random() * 2) + 1), Blocks.LOG);
            }
        }
    }

    void generateTrunkRoots() {
        BlockPosition trunkBase = this.basePos;
        boolean thick = trunkSize > 1;
        int count = (int) (Math.random() * (thick ? 5 : 3)) + 1;
        for (int k = 0; k < count; k++) {
            BlockPosition pos = trunkBase;
            if (thick) {
                switch ((int) (Math.random() * 4)) {
                    case 0:
                    default:
                        break;
                    case 1:
                        pos = pos.east();
                        break;
                    case 2:
                        pos = pos.east().south();
                        break;
                    case 3:
                        pos = pos.south();
                        break;
                }
            }
            int height = (int) (Math.random() * 2 + (thick ? 1 : 0)); // either 1 or 2 blocks above grass
            //dest should be equal or less
            int destHeight = Math.random() < 0.5 ? 1 : 0; // either in grass or on grass
            int dx = (int) ((Math.random() < 0.5 ? 1 : -1) * (trunkSize + Math.random() * (thick ? 3 : 2)));
            int dz = (int) ((Math.random() < 0.5 ? 1 : -1) * (trunkSize + Math.random() * (thick ? 3 : 2)));
            int min = height - destHeight;
            if (dx < min)
                dx = min;
            if (dz < min)
                dz = min;
            BlockPosition start = new BlockPosition(pos.getX(), pos.getY() + height, pos.getZ());
            BlockPosition dest = new BlockPosition(pos.getX() + dx, pos.getY() + destHeight, pos.getZ() + dz);
            int[] diffs = new int[] { dest.getX() - start.getX(), dest.getY() - start.getY(), dest.getZ() - start.getZ() };
            boolean xPos = diffs[0] >= 0;
            boolean yPos = diffs[1] >= 0;
            boolean zPos = diffs[2] >= 0;
            diffs[0] = Math.abs(diffs[0]);
            diffs[1] = Math.abs(diffs[1]);
            diffs[2] = Math.abs(diffs[2]);
            BlockPosition last = start;
            while (diffs[0] > 0 || diffs[1] > 0 || diffs[2] > 0) {
                boolean changed = false;
                if (diffs[0] > 0 && Math.random() < 0.6) {
                    diffs[0]--;
                    start = start.a(xPos ? 1 : -1, 0, 0);
                    changed = true;
                }
                if (diffs[1] > 0 && Math.random() < (thick ? 0.35 : 0.6)) {
                    diffs[1]--;
                    start = start.a(0, yPos ? 1 : -1, 0);
                    changed = true;
                }
                if (diffs[2] > 0 && Math.random() < 0.6) {
                    diffs[2]--;
                    start = start.a(0, 0, zPos ? 1 : -1);
                    changed = true;
                }
                if (changed) {
                    BlockLogAbstract.EnumLogRotation localEnumLogRotation = b(start, last);
                    last = start;
                    a(this.worldObj, start, Blocks.LOG.getBlockData().set(BlockLogAbstract.AXIS, localEnumLogRotation));
                }
            }

        }
    }

    /**
     * Generates additional wood blocks to fill out the bases of different leaf nodes that would otherwise degrade.
     */
    void generateLeafNodeBases() {
        for (Position pos : this.leafNodes) {
            //            int n = (int)(pos.height() * 0.7);
            int n = pos.height() - (int) (Math.random() * 4);
            // connect node to center, same height
            BlockPosition blockPos = new BlockPosition(this.basePos.getX(), n, this.basePos.getZ());
            if (!blockPos.equals(pos)) {
                if (Math.random() > 0.30)
                    continue;
                // shorter branches by 0.6
                int tempX = this.basePos.getX() + (int) ((pos.getX() - this.basePos.getX()) * 0.7);
                int tempZ = this.basePos.getZ() + (int) ((pos.getZ() - this.basePos.getZ()) * 0.7);
                BlockPosition tempPos = new BlockPosition(tempX, n, tempZ);
                placeBlockLine(blockPos, tempPos, Blocks.LOG);
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
            if (!this.worldObj.getType(localBlockPosition2).getBlock().getBlockData().getMaterial().isSolid())
                continue;
            if (!a(this.worldObj.getType(localBlockPosition2).getBlock())) {
                return i1;
            }
        }

        return -1;
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

    private static ArrayList<GenData[]> history = new ArrayList<GenData[]>();

    private static class GenData {
        World world;
        BlockPosition blockPos;
        IBlockData ibd;
        IBlockData orig;
    }

    private ArrayList<GenData> localHist = new ArrayList<GenData>();

    public static boolean undo() {
        if (history.size() < 1)
            return false;
        GenData[] arr = history.remove(history.size() - 1);
        for (GenData gd : arr) {
            gd.world.setTypeAndData(gd.blockPos, gd.orig, 2);
        }
        return true;
    }

    @Override
    protected void a(World world, BlockPosition blockPos, IBlockData ibd) {
        GenData gd = new GenData();
        gd.world = world;
        gd.blockPos = blockPos;
        gd.ibd = ibd;
        gd.orig = world.getChunkAtWorldCoords(blockPos).getBlockData(blockPos);
        localHist.add(gd);
        //        super.a(world, blockPos, ibd);
    }

    private void executeQueued() {
        for (GenData gd : localHist)
            super.a(gd.world, gd.blockPos, gd.ibd);
    }

    public boolean generate(World paramWorld, Random paramRandom, BlockPosition paramBlockPosition) {
        this.worldObj = paramWorld;
        this.basePos = paramBlockPosition;

        this.rand = new Random(paramRandom.nextLong());

        if (this.heightLimit == 0) {
            this.heightLimit = (int) (Math.random() * 20) + 13;
        }

        if (this.heightLimit < 19)
            this.trunkSize = 1;

        this.scaleWidth = 0.9 + (this.height / 100.0 * 1.5);

        if (!validTreeLocation()) {
            return false;
        }

        generateLeafNodeList();
        generateLeaves();
        generateTrunk();
        generateLeafNodeBases();
        generateTrunkRoots();
        generateTrunkBase();

        history.add(localHist.toArray(new GenData[localHist.size()]));
        executeQueued();

        return true;
    }

}
