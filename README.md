<center>

[![Modrinth Icon](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy-minimal/available/modrinth_vector.svg)](https://modrinth.com/project/v4yw0pI3)
[![Github Icon](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy-minimal/available/github_vector.svg)](https://github.com/Solmeye/Eslium-)
[![Discord Icon](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy-minimal/social/discord-singular_vector.svg)](https://discord.gg/FVq3j5heAc)

[![Fabric Icon](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy-minimal/supported/fabric_vector.svg)](https://fabricmc.net/)
[![Quilt Icon](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy-minimal/supported/quilt_vector.svg)](https://quiltmc.org/)
[![NeoForge Icon](https://raw.githubusercontent.com/Solmeye/Eslium-/2b0ed26bc9fd49f2f1c6b8aa289dbb81cd0db956/neoforge.svg)](https://neoforged.net/)

</center>

---

Eslium is a server-side anti-cheat designed to help server administrators detect cheating, malicious behavior, and alternate accounts, while providing tools to investigate.

The project focuses on being compatible with the latest Minecraft version (26.1+) and avoiding any false flag.

This project uses [mixins](https://docs.fabricmc.net/develop/mixins/bytecode) to filter and analyze at the packet level.
However, these mixins are currently not at a low enough level to be completely compatible with other mods

### Next updates

- Default configuration fix
- Support for Paper, Spigot, Folia, Bukkit, Sponge and Folia
- Forge support
- Configuration for ingoing and outgoing packets
- Option to disable the auto antialt check
- `/eslium freeze` command
- Other (You too can also suggest)

## Filtered packages (ingoing)

_The following incoming packets are scanned for impossible or suspicious behavior_

[None]

## Filtered packages (outgoing)

_Subsequent outgoing packets are filtered and fake the data when a Vanilla client cannot distinguish the difference.
This is done in order to prevent or limit the effectiveness of a cheat_

<details>
<summary>ClientboundPlayerCombatEndPacket</summary>

- Packet

> Cancelled

</details>

<details>
<summary>ClientboundPlayerCombatEnterPacket</summary>

- Packet

> Cancelled

</details>

<details>
<summary>ClientboundSetExperiencePacket</summary>

- Field `ExperienceProgress`

> Lossless scrambling value accuracy

> [To implement] Hides the value when it is not supposed to be exploitable (eg. when the locator bar is used)

- Field `TotalExperience`

> Hides the value

</details>

<details>
<summary>ClientboundSetHealthPacket</summary>

- Packet

> Canceled when the packet only update the saturation

- Field `Health`

> Lossless scrambling value accuracy

> [To implement] Hides the value when it is not supposed to be exploitable (eg. when the gamemode is creative)

- Field `Food`

> [To implement] Hides the value when it is not supposed to be exploitable (eg. when riding a horse)

- Field `Saturation`

> Hides the value

</details>

## Commands

- `/eslium`
  Displays the mod version

- `/eslium info`
  Displays the mod version

- `/eslium version`
  Displays the mod version

- `/eslium discord`
  Displays the Discord link

- `/eslium modrinth`
  Displays the Modrinth link

- `/eslium reload`
  Reload the configuration

- `/eslium profile <targets>`
  Displays a player's profile (IP, latency, brand...)

- `/eslium alt <targets>`
  Try to detect alternative accounts

- `/eslium crash <targets>`
  Crashes a player's client

- `/eslium schedule <command> <time>`
  Schedules the execution of any command

- `/eslium data <targets> <path> <value>`
  Writes data to the Json file associated with the player.s
  See https://datatracker.ietf.org/doc/html/rfc6901

- `/eslium webhook <message>`
  Send a message through the webhook

- `/eslium resolve <targets> <key>`
  Resolves [translation keys](https://bugs.mojang.com/browse/MC/issues/MC-265322). Useful for detecting some resource packs or mods.

## Configuration

A default configuration is provided.

<details>
<summary>Default configuration</summary>

```
{
  "key": [
    {
      "identifier": "minecraft",
      "keys": [
        "key.forward",
        "key.left",
        "key.back",
        "key.right",
        "key.jump",
        "key.sneak",
        "key.sprint",
        "key.inventory",
        "key.swapOffhand",
        "key.drop",
        "key.use",
        "key.attack",
        "key.pickItem",
        "key.chat",
        "key.playerlist",
        "key.command",
        "key.socialInteractions",
        "key.screenshot",
        "key.togglePerspective",
        "key.smoothCamera",
        "key.fullscreen",
        "key.advancements",
        "key.quickActions",
        "key.toggleGui",
        "key.toggleSpectatorShaderEffects",
        "key.hotbar.1",
        "key.hotbar.2",
        "key.hotbar.3",
        "key.hotbar.4",
        "key.hotbar.5",
        "key.hotbar.6",
        "key.hotbar.7",
        "key.hotbar.8",
        "key.hotbar.9",
        "key.saveToolbarActivator",
        "key.loadToolbarActivator",
        "key.spectatorOutlines",
        "key.spectatorHotbar",
        "key.debug.overlay",
        "key.debug.modifier",
        "key.debug.crash",
        "key.debug.reloadChunk",
        "key.debug.showHitboxes",
        "key.debug.clearChat",
        "key.debug.showChunkBorders",
        "key.debug.showAdvancedTooltips",
        "key.debug.copyRecreateCommand",
        "key.debug.spectate",
        "key.debug.switchGameMode",
        "key.debug.debugOptions",
        "key.debug.focusPause",
        "key.debug.dumpDynamicTextures",
        "key.debug.reloadResourcePacks",
        "key.debug.profiling",
        "key.debug.copyLocation",
        "key.debug.dumpVersion",
        "key.debug.profilingChart",
        "key.debug.fpsCharts",
        "key.debug.networkCharts",
        "key.debug.lightmapTexture"
      ],
      "commandsOnEachFound": [
        "eslium data %s \"fingerprint/translations/%c/0\" \"%t\"",
        "eslium data %s \"fingerprint/translations/%c/1\" \"%k\""
      ],
      "commandsOnEachNotFound": [
        "kick %s Please remove your anti translation exploit software"
      ],
      "autoresolve": true
    }
  ],
  "antiAlt": {
    "scoreSuspicious": 65,
    "scoreConfirmed": 90,
    "scoreNormalCommand": [],
    "scoreSuspiciousCommand": [
      "eslium webhook \"`%s` is suspected of alting with [%i](https://namemc.com/%i)\\\\n-# Matching score : `%m`\""
    ],
    "scoreConfirmedCommand": [
      "eslium webhook \"`%s` is probably alting with [%i](https://namemc.com/%i)\\\\n-# Matching score : `%m`\\\\n-# Automatic action executed\"",
      "eslium crash %s",
      "ban %s Alting"
    ]
  },
  "delayAntiAltCheck": 30,
  "ingoingFilter": {},
  "outgoingFilter": {
    "ClientboundSetHealthPacket": {
      "enabled": true,
      "options": {
        "fakeSaturation": 20.0
      }
    }
  },
  "webhook": "",
  "version": 1
}
```

</details>

<details>
<summary>Explanation</summary>

- `key`

  - `identifier` Identifier where the key comes from.
    Used for display purposes only

  - `keys` Key list

  - `commandsOnEachFound` Command to execute for each key when resolved by autoresolve

  - `commandsOnEachNotFound` Command to execute for each key when not resolved by autoresolve

  - `autoresolve` Automatically resolves this key group when connecting a player or not

- `antiAlt`

  - `scoreSuspicious` Threshold to execute the commands associated. When the matching alt value is higher than `scoreSuspicious`, it means that the match is suspicious and needs investigation. Min `0` Max `100`

  - `scoreConfirmed` Threshold to execute the commands associated. When the matching alt value is higher than `scoreConfirmed`, it means that the match is confirmed an is an alternative account. Min `0` Max `100`

  - `scoreNormalCommand` Commands executed when the matching score is not suspicious or confirms an alt

  - `scoreSuspiciousCommand` Commands executed when the matching score is suspicious

  - `scoreConfirmedCommand` Commands executed when the matching score confirms an alt.

- `delayAntiAltCheck`
  (In ticks) Delay before checking for alts after that a player joined your server. Higher values makes it easier to disconnect to avoid the scan.

- `webhook`
  Webhook of your Discord Webhook

- `version`
  Version of the configuration. Do not touch!

</details>

## AntiAlt

The antialt system is not explained in detail to avoid easy bypass.

## FAQ

### How does this mod work?

Eslium modifies outgoing network packets to remove or alter data that a vanilla client does not rely on. This is intended to reduce unnecessary information exposure without affecting normal gameplay.

### Is it possible to have a false flag?

Normally, no, with the default values.

### What is the impact on performance?

Currently, no statistics have been compiled on the CPU impact. The impact can only be negative.
However, due to the reduction in packets sent, bandwidth can be decreased.

### Will this mod backport to previous versions of Minecraft 26.1?

No.

### Can I install it on my client?

Yes. Please note, however, that this project may not work correctly if done.

### What version should I update this project to?

Alpha versions may have unstable behavior.

Beta versions are relatively stable and will not pose a problem.

Releases are the most stable.

It is recommended to update this project to the latest beta or stable version, as appropriate

### Is this compatible with [mod]?

To date, no incompatibilities have been reported. If you encounter any, please let me know.
