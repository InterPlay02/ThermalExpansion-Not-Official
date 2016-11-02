package cofh.thermalexpansion.core;

import codechicken.lib.model.ModelRegistryHelper;
import codechicken.lib.texture.TextureUtils;
import codechicken.lib.texture.TextureUtils.IIconRegister;
import cofh.core.render.IconRegistry;
import cofh.core.render.RenderItemModular;
import cofh.thermalexpansion.block.EnumType;
import cofh.thermalexpansion.block.TEBlocks;
import cofh.thermalexpansion.block.cache.BlockCache;
import cofh.thermalexpansion.block.cell.BlockCell;
import cofh.thermalexpansion.block.device.BlockDevice;
import cofh.thermalexpansion.block.dynamo.BlockDynamo;
import cofh.thermalexpansion.block.ender.BlockEnder;
import cofh.thermalexpansion.block.light.BlockLight;
import cofh.thermalexpansion.block.machine.BlockMachine;
import cofh.thermalexpansion.block.plate.BlockPlate;
import cofh.thermalexpansion.block.simple.BlockFrame;
import cofh.thermalexpansion.block.simple.BlockGlass;
import cofh.thermalexpansion.block.tank.BlockTank;
import cofh.thermalexpansion.client.IItemStackKeyGenerator;
import cofh.thermalexpansion.client.bakery.BlockBakery;
import cofh.thermalexpansion.client.model.TEBakedModel;
import cofh.thermalexpansion.item.TEAugments;
import cofh.thermalexpansion.item.TEItems;
import cofh.thermalexpansion.item.tool.ItemPump;
import cofh.thermalexpansion.item.tool.ItemTransfuser;
import cofh.thermalexpansion.render.*;
import cofh.thermalexpansion.render.entity.RenderEntityFlorb;
import cofh.thermalexpansion.render.item.RenderItemFlorb;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Locale;

public class ProxyClient extends Proxy {

    public static RenderItemModular rendererComponent = new RenderItemModular();
    public static RenderItemFlorb rendererFlorb = new RenderItemFlorb();

    @Override
    public void registerRenderInformation() {

        //MinecraftForgeClient.registerItemRenderer(TEItems.itemDiagram, rendererComponent);
        //MinecraftForgeClient.registerItemRenderer(TEFlorbs.itemFlorb, rendererFlorb);

        //ItemRenderRegistry.addItemRenderer(TEItems.diagramSchematic, RenderSchematic.instance);
    }

    @Override
    public void preInit() {

        RenderStrongbox.registerRenderers();

        TEAugments.itemAugment.registerModelVariants();
        TEItems.itemMaterial.registerModelVariants();
        TEItems.itemCapacitor.registerModelVariants();

        registerToolModel(TEItems.itemBattleWrench, "battleWrench");
        registerToolModel(TEItems.itemChiller, "chiller");
        registerToolModel(TEItems.toolDebugger, "debugger");
        registerToolModel(TEItems.itemIgniter, "igniter");
        registerToolModel(TEItems.toolMultimeter, "multimeter");
        registerToolModel(TEItems.itemWrench, "wrench");

        final ModelResourceLocation pumpInput = getToolLocation("pumpinput");
        final ModelResourceLocation pumpOutput = getToolLocation("pumpoutput");
        ModelLoader.setCustomMeshDefinition(TEItems.itemPump, new ItemMeshDefinition() {
            @Override
            public ModelResourceLocation getModelLocation(ItemStack stack) {
                return TEItems.itemPump.getMode(stack) == ItemPump.OUTPUT ? pumpOutput : pumpInput;
            }
        });
        ModelLoader.registerItemVariants(TEItems.itemPump, pumpInput, pumpOutput);

        final ModelResourceLocation transfuserInput = getToolLocation("transfuserinput");
        final ModelResourceLocation transfuserOutput = getToolLocation("transfuseroutput");
        ModelLoader.setCustomMeshDefinition(TEItems.itemTransfuser, new ItemMeshDefinition() {
            @Override
            public ModelResourceLocation getModelLocation(ItemStack stack) {
                return TEItems.itemTransfuser.getMode(stack) == ItemTransfuser.OUTPUT ? transfuserOutput : transfuserInput;
            }
        });
        ModelLoader.registerItemVariants(TEItems.itemTransfuser, transfuserInput, transfuserOutput);

        registerBlockBakeryStuff(TEBlocks.blockMachine, "thermalexpansion:blocks/machine/machine_side", BlockMachine.TYPES);
        registerBlockBakeryStuff(TEBlocks.blockDevice, "thermalexpansion:blocks/device/device_side", BlockDevice.TYPES);
        registerBlockBakeryStuff(TEBlocks.blockDynamo, "", BlockDynamo.TYPES, RenderDynamo.instance);
        registerBlockBakeryStuff(TEBlocks.blockCell, "", BlockCell.TYPES, RenderCell.instance);
        registerBlockBakeryStuff(TEBlocks.blockTank, "", BlockTank.TYPES, RenderTank.instance);
        registerBlockBakeryStuff(TEBlocks.blockCache, "", BlockCache.TYPES);
        registerBlockBakeryStuff(TEBlocks.blockTesseract, "", BlockEnder.TYPES, RenderTesseract.instance);
        registerBlockBakeryStuff(TEBlocks.blockPlate, "", BlockPlate.TYPES, RenderPlate.instance);
        registerBlockBakeryStuff(TEBlocks.blockLight, "", BlockLight.TYPES, RenderLight.instance);
        BlockBakery.registerItemKeyGenerator(Item.getItemFromBlock(TEBlocks.blockLight), new IItemStackKeyGenerator() {
            @Override
            public String generateKey(ItemStack stack) {
                StringBuilder builder = new StringBuilder();
                builder.append(stack.getMetadata());
                builder.append(",");
                builder.append(stack.getItem().getRegistryName().toString());
                builder.append(",");
                if (stack.hasTagCompound()){
                    builder.append(stack.getTagCompound().getByte("Style"));
                }
                return builder.toString();
            }
        });

        registerBlockBakeryStuff(TEBlocks.blockFrame, "", BlockFrame.TYPES, RenderFrame.instance);

        for (EnumType type : EnumType.values()) {
            ModelResourceLocation location = new ModelResourceLocation(TEBlocks.blockWorkbench.getRegistryName(), "type=" + type.getName().toLowerCase(Locale.US));
            ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(TEBlocks.blockWorkbench), type.ordinal(), location);
        }

        for (BlockGlass.Types type : BlockGlass.Types.values()){
            ModelResourceLocation location = new ModelResourceLocation(TEBlocks.blockGlass.getRegistryName(), "type=" + type.getName().toLowerCase(Locale.US));
            ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(TEBlocks.blockGlass), type.ordinal(), location);
        }

        for (EnumDyeColor color : EnumDyeColor.values()){
            ModelResourceLocation location = new ModelResourceLocation(TEBlocks.blockRockwool.getRegistryName(), "color=" + color.getName().toLowerCase(Locale.US));
            ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(TEBlocks.blockRockwool), color.ordinal(), location);
        }
    }

    private static void registerBlockBakeryStuff(Block block, String particle, IProperty typeProperty, IProperty... ignores) {
        IIconRegister register = block instanceof IIconRegister ? ((IIconRegister) block) : null;
        registerBlockBakeryStuff(block, particle, typeProperty, register, ignores);
    }

    //TODO Override the particle grabbing.
    private static void registerBlockBakeryStuff(Block block, String particle, IProperty typeProperty, IIconRegister iconRegister, IProperty... ignores) {
        StateMap.Builder builder = new StateMap.Builder();
        builder.ignore(typeProperty);
        for (IProperty property : ignores) {
            builder.ignore(property);
        }

        StateMap stateMap = builder.build();
        ModelLoader.setCustomStateMapper(block, stateMap);
        IBakedModel model = new TEBakedModel(particle);
        ModelResourceLocation locationNormal = new ModelResourceLocation(block.getRegistryName(), "normal");
        ModelResourceLocation locationInventory = new ModelResourceLocation(block.getRegistryName(), "inventory");
        for (int i = 0; i < typeProperty.getAllowedValues().size(); i++) {
            ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), i, locationInventory);
        }

        ModelRegistryHelper.register(locationNormal, model);
        ModelRegistryHelper.register(locationInventory, model);

        if (iconRegister != null) {
            TextureUtils.addIconRegister(iconRegister);
        }
    }

    private ModelResourceLocation getToolLocation(String name) {
        return new ModelResourceLocation("thermalexpansion:tool", "type=" + name.toLowerCase());
    }

    private void registerToolModel(Item item, String name){
        registerToolModel(item, 0, name);
    }

    private void registerToolModel(ItemStack stack, String name) {
        registerToolModel(stack.getItem(), stack.getMetadata(), name);
    }

    private void registerToolModel(Item item, int metadata, String name) {
        final ModelResourceLocation location = getToolLocation(name);
        ModelLoader.setCustomModelResourceLocation(item, metadata, location);
    }

    @Override
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void registerIcons(TextureStitchEvent.Pre event) {

        IconRegistry.addIcon("IconConfigTesseract", "thermalexpansion:items/icons/icon_Config_Tesseract", event.getMap());
        IconRegistry.addIcon("IconRecvOnly", "thermalexpansion:items/icons/icon_recvonly", event.getMap());
        IconRegistry.addIcon("IconSendOnly", "thermalexpansion:items/icons/icon_sendonly", event.getMap());
        IconRegistry.addIcon("IconSendRecv", "thermalexpansion:items/icons/icon_sendrecv", event.getMap());
        IconRegistry.addIcon("IconBlocked", "thermalexpansion:items/icons/icon_blocked", event.getMap());
        IconRegistry.addIcon("IconSchematic", "thermalexpansion:items/diagram/schematic", event.getMap());
        IconRegistry.addIcon("IconSlotSchematic", "thermalexpansion:items/icons/Icon_SlotSchematic", event.getMap());

    }

    @Override
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void initializeIcons(TextureStitchEvent.Post event) {

        RenderCache.initialize();
        RenderCell.initialize();
        RenderDynamo.initialize();
        RenderFrame.initialize();
        RenderPlate.initialize();
        RenderLight.initialize();
        RenderSponge.initialize();
        RenderStrongbox.initialize();
        RenderTank.initialize();
        RenderTesseract.initialize();

        RenderItemFlorb.initialize();

        RenderEntityFlorb.initialize();

    }

}
