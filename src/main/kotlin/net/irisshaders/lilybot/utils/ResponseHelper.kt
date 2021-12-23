package net.irisshaders.lilybot.utils

import dev.kord.core.entity.User
import dev.kord.common.Color
import dev.kord.rest.builder.message.EmbedBuilder

fun responseEmbed(title: String, requester: User, color: Color, block: EmbedBuilder.() -> Unit): UserMessageCreateBuilder.() -> Unit {
    val ret = EmbedBuilder()
    ret.title = title
    ret.color = color
    ret.footer{
        text = "Requested by " + requester.tag
        iconUrl = requester.avatar?.url
    }
    timestamp = Clock.System.now()
    ret.apply(block)
    return ret.build()
}