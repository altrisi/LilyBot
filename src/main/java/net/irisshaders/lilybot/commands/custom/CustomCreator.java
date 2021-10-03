package net.irisshaders.lilybot.commands.custom;

import com.jagrosh.jdautilities.command.SlashCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.privileges.CommandPrivilege;
import net.irisshaders.lilybot.LilyBot;
import net.irisshaders.lilybot.utils.Constants;

import java.awt.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class CustomCreator extends SlashCommand {

    public CustomCreator() {
        this.name = "createcustom";
        this.help = "Creates a custom command.";
        this.defaultEnabled = true;
        this.enabledRoles = new String[]{Constants.MODERATOR_ROLE}; //idk what to use here
        this.guildOnly = true;
        this.botPermissions = new Permission[]{Permission.MESSAGE_WRITE};
        this.botMissingPermMessage = "The bot can't write messages!";
        List<OptionData> optionData = new ArrayList<>();
        // Adds an option to the array list of type Boolean. Named hi, and has a description
        optionData.add(new OptionData(OptionType.STRING, "command", "The name of the command").setRequired(true));
        optionData.add(new OptionData(OptionType.STRING, "help", "The help message of the command").setRequired(true));
        optionData.add(new OptionData(OptionType.STRING, "title", "The title of the response").setRequired(true));
        optionData.add(new OptionData(OptionType.STRING, "desc", "The body of the response").setRequired(true));
        this.options = optionData; // Sets the options to be the optionData array
    }

    @Override
    protected void execute(SlashCommandEvent event) {
        User user = event.getUser(); // Get's the user that ran the command
        String commandName = event.getOption("command").getAsString();
        String commandHelp = event.getOption("help").getAsString();
        String commandTitle = event.getOption("title").getAsString();
        String commandDesc = event.getOption("desc").getAsString();
        MessageEmbed embed = new EmbedBuilder() // Creates a builder for the embed, name "builder" in this case
                .setTitle("Command created!")
                .setDescription(
                        "The command should be usable Soon. Probably. We'll see"
                )
                .setColor(Color.RED) // Java AWT Color is used here. It may not have to be, unknown
                .setFooter("Requested by " + user.getAsTag(), user.getEffectiveAvatarUrl()) // Get's the user's tag and their Profile picture and adds them to the footer
                .setTimestamp(Instant.now()) // Get's the timer the command was sent. This will be lined up with any user's System time
                .build(); // Builds the embed
         
        event.replyEmbeds(embed).mentionRepliedUser(false).setEphemeral(false).queue();
        SlashCommand cmd = new Custom(commandName, commandHelp, commandTitle, commandDesc, new SlashCommand[0], Color.RED);
        //Copy of CommandClientImpl
        LilyBot.INSTANCE.commandClient.addSlashCommand(cmd);
        CommandData data = cmd.buildCommandData();
        Guild guild = LilyBot.INSTANCE.jda.getGuildById(Constants.GUILD_ID);
        List<CommandPrivilege> privileges = cmd.buildPrivileges(LilyBot.INSTANCE.commandClient);
        guild.upsertCommand(data).queue(command1 -> {
            if (!privileges.isEmpty())
                command1.updatePrivileges(guild, privileges).queue();
        });
        //End copy
        LilyBot.LOG_LILY.info(user.getAsTag()+" created command '"+commandName+"'");
    }
}
