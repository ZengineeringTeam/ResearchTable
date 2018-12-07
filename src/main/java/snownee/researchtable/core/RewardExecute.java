package snownee.researchtable.core;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class RewardExecute implements IReward
{
    String[] commands;

    public RewardExecute(String... commands)
    {
        this.commands = commands;
    }

    @Override
    public void earn(World world, BlockPos pos, EntityPlayer player)
    {
        ICommandSender sender = new ICommandSender()
        {
            @Override
            public MinecraftServer getServer()
            {
                return player.getServer();
            }

            @Override
            public String getName()
            {
                return player.getName();
            }

            @Override
            public World getEntityWorld()
            {
                return player.getEntityWorld();
            }

            @Override
            public boolean canUseCommand(int permLevel, String commandName)
            {
                return permLevel <= 2;
            }

            @Override
            public BlockPos getPosition()
            {
                return pos;
            }

            @Override
            public Vec3d getPositionVector()
            {
                return new Vec3d(pos).add(0.5, 0.5, 0.5);
            }

            @Override
            public Entity getCommandSenderEntity()
            {
                return player;
            }

            @Override
            public boolean sendCommandFeedback()
            {
                return false;
            }
        };

        for (String command : commands)
            player.getServer().getCommandManager().executeCommand(sender, command);
    }

}
