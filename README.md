<center>

## Eslium

[![Modrinth Icon](https://cdn.sqidgeon.uk/cozy/available/modrinth_vector.svg)](https://modrinth.com/project/zaVwh7an)
[![Github Icon](https://cdn.sqidgeon.uk/cozy/available/github_vector.svg)](https://github.com/Solmeye/Eslium)
[![Discord Icon](https://cdn.sqidgeon.uk/cozy/social/discord-plural_vector.svg)](https://discord.gg/FVq3j5heAc)

[![Fabric Icon](https://cdn.sqidgeon.uk/cozy/supported/fabric_vector.svg)](https://fabricmc.net/)
[![Quilt Icon](https://cdn.sqidgeon.uk/cozy/supported/quilt_vector.svg)](https://quiltmc.org/)
[![NeoForge Icon](https://cdn.sqidgeon.uk/cozy/supported/neoforge_vector.svg)](https://neoforged.net/)
[![Forge Icon](https://cdn.sqidgeon.uk/cozy/supported/forge_vector.svg)](https://files.minecraftforge.net/net/minecraftforge/forge/)

</center>

Eslium is a project to predict more things client-side.
For example, if you want to use a Crystal, you must wait for the server validation for it to appear.
This mod fixes that and therefore compensates for the ping.

## Predictions implemented

<details>
<summary>Minecarts</summary>

When using any type of minecart on a rail, the spawn of the minecart is predicted

</details>

<details>
<summary>End Crystal</summary>

When using an end crystal on obisidian or bedrock, the spawn of the end crystal is predicted

</details>

## Predictions ComingSoon™ :

<details>
<summary>Predictions ComingSoon™</summary>

- Lunge enchant
- Anchors
- Cooldowns
- Consumables
- Elytra
- Firework rocket
- Swap
- Inventory
- Cushion
- Entity pose
- Wind charge
- Jukebox
- Crossbow
- Pickup entities
- Trident
- Weapons
- Jukebox
- Bottle o' Enchanting
- Experience
- Ender Pearls
- Potions
- Pick Block
- Cactus damage
- Bed
- Fishing Rod
- Lead
- Boat
- Snowball
- Spawn Eggs
- Egg
- Armor Stand
- Note block
- Item frame
- Dyes
- Interfaces
- Void damage
- Game mode change
- Potion Effects
- Tchat
- Sounds
- Crafting
- Scaffolding
- Knockback
- Fall damage

</details>

## Configuration

<details>
<summary>Default configuration</summary>

```
{
  "enabled": true,
  "version": 1,
  "crystal": {
    "enabled": true
  },
  "minecart": {
    "enabled": true
  },
  "simulatedDesync": 50
}
```

</details>

<details>
<summary>Explanation</summary>

- `enabled` Enable or disable the mod

- `version` Version of the configuration. Do not touch!

- `crystal`
  - `enabled` Enable or disable the crystal prediction

- `minecart`
  - `enabled` Enable or disable the minecart prediction

- `simulatedDesync` Percentage of maximum client-server tick desynchronization time used to simulate vanilla desynchronization. Useful for replicating vanilla desynchronization and ensuring statistical fairness. Note that in singleplayer this would be `0`, and on a local server this would be at `50` on average<br>`min`: 0 `max`: 100


</details>

## FAQ
### How it works ?
This mod uses mixins to inject code into Minecraft code. During certain actions, if implemented, the behavior of a vanilla server is replicated on the client side. 

### Is this mod a cheat?
You might think this mod is a cheat because it gives an advantage to those who don't have it, and these kinds of mods often raise these kinds of questions.

Here are a few points :

- Some people naturally have lower ping, yet it is not considered cheating

- It is possible to have 0ms ping behavior in vanilla, but it is also possible to have it (as far as it is implemented) with any ping and Eslium.

- Some mods remain accepted by the community but their behavior is impossible to reproduce in vanilla.
I'm thinking in particular of Health Indicator, Armor HUD, AppleSkin, FreeCam, and Ok Zoomer.

- This mod has been accepted by Modrinth and some PvP servers, such as
  - `PvPClub`
  - `FadedMC`
  - `Turtled`
  - `CatPvP`
  - `Minemen`

- Sodium optimize FPS, Lithium optimize MSPT / TPS, Eslium optimize the ping's impact.

- Other ping's optimizer are allowed, such as Marlow's Crystal Optimizer, Hero's Elytra Optimizer, Consumable Optimizer or Anchor Optimizer

### Is this likely to break the desynchronization of the client-server tick loop?
No. This desynchronization is simulated by an artificial time delay of half a tick.

### Am I going to flag the anticheats?
Versions 4.0+ are designed to not trigger anticheats.
The way it works is by only adding the entities client-side when they are rendered and at the moment of their ticking, without letting them interacting with anything real.
So no, you won't if you use the recommended versions.

Note: I played for months with this mod and I have never been banned

### Which branch should I install?
- Release versions are safe to use

- Beta might behave weirdly but shouldn't flag any anticheat

- Alpha versions are unstable and are only here to test the new functionnalities and fixes, regardless of their stability.

For a casual player, beta or release versions are recommended.

## Compatibility
Eslium should work on any client.

## Support me!
Join my Hardcore Minecraft server: [HardcoreSMP](https://modrinth.com/server/hardcoresmp_) - `38.143.19.130`
