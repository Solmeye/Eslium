<details>
<summary>Old documentation (v3-)</summary>

This mod compensates for :
- Consumables
- End Crystal
- Elytras
- Firework rocket
- Cooldowns
- Entity pose
- Weapons
- Minecart
- Swap
- Wind charge

<details>
<summary>Explanations of the predictions</summary>

> Consumables

Allows consumption to be completed without delay

> Cooldowns

Removes server-imposed cooldowns (on enderpearls, chorus fruit, etc.)
The client side one remains applied

> End Crystal

Allows crystals to be spawned and destroyed immediately.
Since some servers don't have server-side compensation software, this also allows real crystals to be automatically destroyed upon reception if they were previously destroyed in their client-side state only.
Compatible with servers using Faster Crystals.

> Elytras

Allows you to stop flying without delay

> Firework rocket

Allows you to use a firework rocket without delay when flying in elytra

> Minecarts

Allows minecarts to be spawned without delay (if experimental setting is enabled you can ride it without delay)

> Entity pose

Removes server-imposed poses (flying, sneaking...)

> Swap

Predicts the swap of items between the main and secondary hand
(only works if experimental setting is enabled)

> Weapons

Throw tridents client-side
Finish loading crossbows without delay

> Wind Charge

Launches wind-charges client-side and makes them destroy themselves at maximum height + 30 (as in vanilla)


</details>



<details>
<summary>Configuration (explanations)</summary>
  
- enabled : Enables or disables the option
- optout : Enables or disables the opt-out
- debug : Enables or disables debug mode
- experimental : Enables or disables experimental options
- defaultPing : Default ping
- servers : Allows you to select which server type the given option should work on. CUSTOM / MODDED / PLUGIN / VANILLA
- autoAttack : Automatically destroys a entity when received if a client-side entity located in the exact same location was previously destroyed (especially for crystals)
- bypassRequiredAiming : Ignores certain conditions such as range, item usage (shield/consumable) or aiming when using autoAttack
- maxDistance : Maximum distance in blocks between server-side and client-side actions to bind (prevents unwanted server actions from being considered true by the client)
- hasFasterCrystals : Indicates which server types have a faster crystal by default
</details>


<details>
<summary>Configuration (default)</summary>
  
```
{
  "enabled": true,
  "optout": true,
  "debug": false,
  "experimental": false,
  "defaultPing": 75,
  "consumables": {
    "enabled": true,
    "servers": {
      "CUSTOM": true,
      "MODDED": true,
      "PLUGIN": true,
      "VANILLA": true
    },
    "maxDistance": 2.0
  },
  "cooldowns": {
    "enabled": true,
    "servers": {
      "CUSTOM": true,
      "MODDED": true,
      "PLUGIN": true,
      "VANILLA": true
    }
  },
  "crystals": {
    "enabled": true,
    "servers": {
      "CUSTOM": true,
      "MODDED": true,
      "PLUGIN": true,
      "VANILLA": true
    },
    "autoAttack": {
      "enabled": true,
      "servers": {
        "CUSTOM": false,
        "MODDED": true,
        "PLUGIN": true,
        "VANILLA": true
      },
      "bypassRequiredAiming": {
        "enabled": true,
        "servers": {
          "CUSTOM": false,
          "MODDED": true,
          "PLUGIN": false,
          "VANILLA": true
        }
      }
    },
    "hasFasterCrystals": {
      "enabled": true,
      "servers": {
        "CUSTOM": true,
        "MODDED": false,
        "PLUGIN": false,
        "VANILLA": false
      }
    }
  },
  "elytras": {
    "enabled": true,
    "servers": {
      "CUSTOM": false,
      "MODDED": true,
      "PLUGIN": true,
      "VANILLA": true
    }
  },
  "firework": {
    "enabled": true,
    "servers": {
      "CUSTOM": true,
      "MODDED": true,
      "PLUGIN": true,
      "VANILLA": true
    },
    "maxDistance": 3.0
  },
  "jukebox": {
    "enabled": false,
    "servers": {
      "CUSTOM": true,
      "MODDED": true,
      "PLUGIN": true,
      "VANILLA": true
    }
  },
  "minecart": {
    "enabled": true,
    "servers": {
      "CUSTOM": true,
      "MODDED": true,
      "PLUGIN": true,
      "VANILLA": true
    }
  },
  "pose": {
    "enabled": true,
    "servers": {
      "CUSTOM": true,
      "MODDED": true,
      "PLUGIN": true,
      "VANILLA": true
    }
  },
  "swap": {
    "enabled": true,
    "servers": {
      "CUSTOM": true,
      "MODDED": true,
      "PLUGIN": true,
      "VANILLA": true
    }
  },
  "weapons": {
    "enabled": true,
    "servers": {
      "CUSTOM": true,
      "MODDED": true,
      "PLUGIN": true,
      "VANILLA": true
    },
    "crossbow": {
      "enabled": true,
      "servers": {
        "CUSTOM": true,
        "MODDED": true,
        "PLUGIN": true,
        "VANILLA": true
      }
    },
    "trident": {
      "enabled": true,
      "servers": {
        "CUSTOM": true,
        "MODDED": true,
        "PLUGIN": true,
        "VANILLA": true
      },
      "maxDistance": 3.0
    }
  },
  "windcharge": {
    "enabled": true,
    "servers": {
      "CUSTOM": true,
      "MODDED": true,
      "PLUGIN": true,
      "VANILLA": true
    },
    "maxDistance": 3.0
  }
}
```

</details>


</details>
