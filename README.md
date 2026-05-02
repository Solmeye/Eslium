<center>

[![Modrinth Icon](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy-minimal/available/modrinth_vector.svg)](https://modrinth.com/project/zaVwh7an)
[![Github Icon](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy-minimal/available/github_vector.svg)](https://github.com/Solmeye/Eslium)
[![Discord Icon](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy-minimal/social/discord-singular_vector.svg)](https://discord.gg/FVq3j5heAc)

[![Fabric Icon](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy-minimal/supported/fabric_vector.svg)](https://fabricmc.net/)
[![Quilt Icon](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy-minimal/supported/quilt_vector.svg)](https://quiltmc.org/)
[![NeoForge Icon](https://raw.githubusercontent.com/Solmeye/Mirror-Anticheat/2b0ed26bc9fd49f2f1c6b8aa289dbb81cd0db956/neoforge.svg)](https://neoforged.net/)
[![Forge Icon](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy-minimal/supported/forge_vector.svg)](files.minecraftforge.net)

</center>

___

## What's Eslium ?
Eslium is a project to predict more things client-side.
For example, if you swap items with your secondary hand, you must wait for the server to exchange it for you
This mod fixes that and therefore compensates for the ping.

## Predictions implemented

<details>
<summary>Swap</summary>

When pressing the key to exchange the item from the main hand to the offhand, outside of an inventory, the swap is predicted

</details>


<details>
<summary>Inventory</summary>

When the inventory is resynchronized by the server, if the inventory indicated by the server corresponds to that which the client previously had, the synchronization is canceled

This avoids rollbacks due to delayed resynchronization

</details>


<details>
<summary>Minecarts</summary>

When using any type of minecart on a rail, the spawn of the minecart is predicted

</details>


## Predictions Coming Soon™ :
- Lunge enchant
- End Crystal
- Cooldowns
- Consumables
- Elytra
- Firework rocket
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
- Interaction with blocks
- Interaction with entities
- Interaction with items

## Disclaimer
Versions for 1.21.x are deprecated. I strongly recommend not using them.

Allowed on servers such as:
- `PvPClub`
- `FadedMC`
- `Turtled`
- `CatPvP`

Versions 4.0+ are designed not to trigger anticheats.
However, a bug is not inevitable, so it is still recommended to request authorization for this mod before using it on a server.

## FAQ
### How it works ?
This mod uses mixins to inject code into Minecraft code. During certain actions, if implemented, the behavior of a vanilla server is replicated on the client side. 

### Is this likely to break the desynchronization of the client-server tick loop?
Not really. This desynchronization is replicated by an artificial time delay of half a tick.

### Is this mod a cheat?
You might think this mod is a cheat because it gives an advantage to those who don't have it, and these kinds of mods often raise these kinds of questions.

Here are a few points :

- Some people naturally have lower ping, yet it is not considered cheating

- It is possible to have 0ms ping behavior in vanilla, but it is also possible to have it (as far as it is implemented) with any ping and Eslium.

- Some mods remain accepted by the community but their behavior is impossible to reproduce in vanilla.
I'm thinking in particular of Health Indicator, Armor HUD, AppleSkin, FreeCam, and Ok Zoomer.

- This mod has been accepted by Modrinth and some PvP servers.

- Sodium optimize FPS, Eslium optimize the ping's impact.

- Other ping's optimizer are allowed, such as Marlow's Crystal Optimizer, Hero's Elytra Optimizer, Consumable Optimizer or Anchor Optimizer

### Will versions 4.0+ be backported for minecraft 1.21.x?
Probably. At least for 1.21.11 I think.

## Compatibility
Eslium should work on any client.

## Support me!
Join my Hardcore Minecraft server: `38.143.19.130:51965`
