package snownee.researchtable.core;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import snownee.researchtable.ModConfig;

import javax.annotation.Nullable;

public class RewardExecute implements IReward {
    private final String[] commands;

    public RewardExecute(String... commands) {
        this.commands = commands;
    }

    @Override
    public void earn(World world, BlockPos pos, EntityPlayer player) {
        // Use player entity as ICommandSender directly if non-privileged mode is enabled.
        // Used for a slightly better compatibility with permission management systems like FTBUtils.
        ICommandSender sender = ModConfig.nonPrivilegedMode ? player : new PrivilegedPlayer(player);
        for (String command : commands) {
            player.getServer().getCommandManager().executeCommand(sender, command);
        }
    }

    static final class PrivilegedPlayer implements ICommandSender {
        private final EntityPlayer player;

        PrivilegedPlayer(EntityPlayer player) {
            this.player = player;
        }

        @Override
        public String getName() {
            return player.getName();
        }

        @Override
        public boolean canUseCommand(int permLevel, String commandName) {
            return permLevel <= 2;
        }

        @Override
        public World getEntityWorld() {
            return player.getEntityWorld();
        }

        @Nullable
        @Override
        public MinecraftServer getServer() {
            return player.getServer();
        }

        @Override
        public BlockPos getPosition() {
            return player.getPosition();
        }

        @Override
        public Vec3d getPositionVector() {
            return player.getPositionVector();
        }

        @Override
        public Entity getCommandSenderEntity() {
            return player;
        }

        @Override
        public boolean sendCommandFeedback() {
            return false;
        }

        /*@Override
        public void sendMessage(ITextComponent component)
        {
            // TODO Instead of spamming message in chat window, can we log it for debugging purpose?
        }*/
    }

}
