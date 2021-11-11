Name: Paul Menexas
Email: pmenexas@bu.edu
BU ID: U78320172

Compilation Instructions:

1) Set your working directory so that it holds the submitted java files.
2) Run > javac *.java
3) Run > java Main

That's it, then play.

Notes / Potential Extra Credit:

There are no txt files used, everything is contained within java files.

Print statements that are useful to the user such as can't move in a direction, can't buy an item, etc are printed first
and then the same screen is reprinted. Please make the screen small enough to see the messages or scroll up a bit. There
are tons of user safety with clear messages.

The Singleton method is used for both the Map and the Player, both of which should only have a single global method.

The Factory method was used to create Heroes for the selection process in the beginning so a Player always has different
options.

The Factory method was used to create different types of Monsters when the player randomly runs into a fight on a
common tile.

The Factory method was used to create a new Market every time it was entered. This is so the items are different for
the player.

The map checks that every single accessible tile is accessible from the start point. I did a recursive search from the
starting point to make sure that all other accessible tiles have a potential path to reach them. This not only handles
the case where a player starts trapped but it also makes sure the map doesn't have wasted space that isn't reachable
at all.

The map is color coordinated, I don't know what that counts for lol.

Check bottom of file for some game design choices, separate from OO design.

Class Descriptions:

Player - This class uses the Singleton method, and it represents the user who actually is playing. The Player picks and
chooses their heroes and then plays the game with them.

Character - This is the parent class that represents all characters involved in the game. Two subclasses extend from
this abstract class.

    Hero - A subclass of Character, Hero represents the objects that the Player will play with. Three types of Heroes
    extend from this class.

        Warrior, Sorcerer, Paladin - Each are a subclass of Hero

    Monster - A subclass of Character, Monster represents the objects that the Player will fight against. Three types
    of Monsters extend from this class.

        Dragon, Exoskeleton, Spirit - Each are a subclass of Monster

Item - The parent class that represent all objects that can be owned by a given Hero. (4 subclasses)

    Armor - A subclass of Item, this object is Equippable. This means a Hero can equip it to reduce damage taken.

    Weapon - A subclass of Item, this object is Equippable. This means a Hero can equip it to inflict more damage.

    Potion - A subclass of Item, this object is Usable. A Hero can use it to increase specific stats.

    Spell - A subclass of Item, this object is Castable. This a parent class to 3 different types of Spells, each
    which implement their own unique cast given a Hero i.e. the Caster, and a Monster, i.e. the one being attacked.

    IceSpell, FireSpell, LightningSpell - Each are a subclass of Spell that implement Castable's cast function.

Castable - An interface that spells, i.e. anything castable implements. A Hero casts a spell on a Monster.

Equippable - An interface that Armor and Weapon both implement. Ensures that they are equipped/unequipped by a Hero
appropriately.

Fightable - An interface that Characters who fight implement, i.e. both Heroes and Monsters. They allow for attacking
and dodging to be attached to a type instead of a class.

Fight - A class that handles all functions tied to the fight including printing the screens, rewarding player, and
coordinating rounds between Heroes and Monsters.

Inventory - A class that holds items for a given Hero and has many useful helpful functions to parse through a Hero's
inventory for specific items. There also some general static methods used for printing the inventory screen itself.

Market - A class that allows a Player to buy / sell items for each of their heroes. The market screen lives in this file
and a new Market is generated every time it is entered, each time with new items thanks to factories.

Map - This class uses the Singleton method, and it represents the map of the game. It is with the map that a player can
switch between tiles and the map screen is printed.

Tile - An enum that contains the different types of tiles that exist within the game. It is within here that they get
painted different colors.

StatType - An enum that contains the different stats that a potion can affect, useful for StatsAffected variable in potion.

CharacterFactoryCreator - The interface that the Hero and Monster factories implement to ultimately create characters.

HeroFactory - A factory class that creates Heroes on the fly as needed, only used in beginning of the game when the
player picks their heroes.

MonsterFactory - A factory class that creates Monsters on the fly as needed, only used when a fight commences and
n monsters need to be generated.

HeroSelection - The class that conducts the hero selection process for the player at the beginning of the game.

ItemFactoryCreator - The interface that the Armor, Weapon, Potion, and Spell factories implement to ultimately create
items that populate the Market.

ArmorFactory - A factory class that creates Armor on the fly as needed, only used in the Market to generate possible
armor to buy.

WeaponFactory - A factory class that creates Weapons on the fly as needed, only used in the Market to generate possible
weapon to buy.

PotionFactory - A factory class that creates Potions on the fly as needed, only used in the Market to generate possible
potions to buy.

SpellFactory - A factory class that creates Spells on the fly as needed, only used in the Market to generate possible
spells to buy.

Main - This class runs the program by creating a player and a map, and then letting the player play until the game is over.

Utils - This class holds some global helper functions and global instances of useful objects.

Game Design Notes:

For equipment, a Hero can either have a one-handed weapon and armor equipped at the same time or just a two-handed weapon.

If the heroes win the fight, fainted heroes that are revived are given full health back.

There is no defense stat for Heroes, just like the specification. The armor, if equipped, serves as a Hero's defense.

Aside from this, the game matches the assignment description.