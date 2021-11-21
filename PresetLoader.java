import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


public class PresetLoader
{
    private List<Armor>   armorCollection;
    private List<Weapon>  weaponCollection;
    private List<Potion>  potionCollection;
    private List<Spell>   spellCollection;
    private List<Monster> monsterCollection;
    private List<Hero>    heroCollection;
    private static PresetLoader  presetLoader = null;
    private PresetLoader() 
    {
        armorCollection   = new ArrayList<>();
        weaponCollection  = new ArrayList<>();
        potionCollection  = new ArrayList<>();
        spellCollection   = new ArrayList<>();
        monsterCollection = new ArrayList<>();
        heroCollection    = new ArrayList<>();
        
        loadPreset();
    }
    
    public static PresetLoader getInstance()
    {
        if(presetLoader == null)
        {
            presetLoader = new PresetLoader();
        }
        return presetLoader;
    }
    
    private void loadPreset() 
    {
        // load armor preset;
        loadArmorPreset();
        loadWeaponPreset();
        loadPotionPreset();
        loadSpellPreset();
        loadMonsterPreset();
        loadHeroPreset();
    }
    

    private void loadArmorPreset()
    {
        List<String> lines = loadConfigFile("Armory.txt");
        for(int i = 1; i < lines.size(); i++)
        {
            String           line = lines.get(i);
            List<String>     items = Arrays.asList(line.split("\\s+"));
            Iterator<String> ite = items.iterator();
            
            String name            = !ite.hasNext() ? "???" : ite.next();
            int    cost            = !ite.hasNext() ? 0     : Integer.valueOf(ite.next());
            int    reqLevel        = !ite.hasNext() ? 0     : Integer.valueOf(ite.next());
            int    damageReduction = !ite.hasNext() ? 0     : Integer.valueOf(ite.next());
            
            Armor armor = new Armor(name, cost, reqLevel, damageReduction);
            this.armorCollection.add(armor);
        }
    }
    
    private void loadWeaponPreset()
    {
        List<String> lines = loadConfigFile("Weaponry.txt");
        for(int i = 1; i < lines.size(); i++)
        {
            String           line = lines.get(i);
            List<String>     items = Arrays.asList(line.split("\\s+"));
            Iterator<String> ite = items.iterator();
            
            String name            = !ite.hasNext() ? "???" : ite.next();
            int    cost            = !ite.hasNext() ? 0     : Integer.valueOf(ite.next());
            int    reqLevel        = !ite.hasNext() ? 0     : Integer.valueOf(ite.next());
            int    damage          = !ite.hasNext() ? 0     : Integer.valueOf(ite.next());
            int    reqHands        = !ite.hasNext() ? 0     : Integer.valueOf(ite.next());
            
            Weapon weapon = new Weapon(name, cost, reqLevel, damage, reqHands == 2 ? true : false);
            this.weaponCollection.add(weapon);
        }
    }
    
    private void loadPotionPreset()
    {
        List<String> lines = loadConfigFile("Potions.txt");
        for(int i = 1; i < lines.size(); i++)
        {
            String           line = lines.get(i);
            List<String>     items = Arrays.asList(line.split("\\s+"));
            Iterator<String> ite = items.iterator();
            
            String name     = !ite.hasNext() ? "???"  : ite.next();
            int    cost     = !ite.hasNext() ? 0      : Integer.valueOf(ite.next());
            int    reqLevel = !ite.hasNext() ? 0      : Integer.valueOf(ite.next());
            int    attrInc  = !ite.hasNext() ? 0      : Integer.valueOf(ite.next());
            String attrs    = !ite.hasNext() ? "None" : ite.next();
            
            List<StatType> potionStatTypes = new LinkedList<>(); 
            if(attrs.contains("All"))
            {
                for(String attr : StatType.getAllAttrs())
                {
                    potionStatTypes.add(getAttrInstance(attr));
                }
            }
            else
            {
                String[] attrArr = attrs.split("/");
                for(String attr : attrArr)
                {
                    potionStatTypes.add(getAttrInstance(attr));
                }
            }
            
            Potion potion = new Potion(name, cost, reqLevel, attrInc, potionStatTypes.toArray(new StatType[potionStatTypes.size()]));
            this.potionCollection.add(potion);
        }
    }
    
    private void loadSpellPreset()
    {
        loadFireSpell();
        loadIceSpell();
        loadLightningSpell();
    }
    
    private void loadLightningSpell()
    {
        loadSpellHelper("LightningSpells.txt");
    }

    private void loadIceSpell()
    {
        loadSpellHelper("IceSpells.txt");
        
    }

    private void loadFireSpell()
    {
        loadSpellHelper("FireSpells.txt");
        
    }
    
    private void loadSpellHelper(String fileName)
    {
        List<String> lines = loadConfigFile(fileName);
        for(int i = 1; i < lines.size(); i++)
        {
            if(lines.get(i).length() == 0)
            {
                continue;
            }
            String           line  = lines.get(i);
            List<String>     items = Arrays.asList(line.split("\\s+"));
            Iterator<String> ite   = items.iterator();
            
            String name        = !ite.hasNext() ? "???" : ite.next();
            int    cost        = !ite.hasNext() ? 0     : Integer.valueOf(ite.next());
            int    reqLevel    = !ite.hasNext() ? 0     : Integer.valueOf(ite.next());
            int    damage      = !ite.hasNext() ? 0     : Integer.valueOf(ite.next());
            int    manaCost    = !ite.hasNext() ? 0     : Integer.valueOf(ite.next());
            Spell spell = null;
            int spellReduceStat = (Utils.rand.nextInt(reqLevel*(Utils.rand.nextInt(2)+2)) + 1) * (Utils.rand.nextInt(101) + 100);
            if(fileName.contains("Fire"))
            {
                spell = new FireSpell(name, cost, reqLevel, damage, manaCost, spellReduceStat);
            }
            if(fileName.contains("Ice"))
            {
                spell = new IceSpell(name, cost, reqLevel, damage, manaCost, spellReduceStat);
            }
            if(fileName.contains("Light"))
            {
                spell = new LightningSpell(name, cost, reqLevel, damage, manaCost, spellReduceStat);
            }
             
            this.spellCollection.add(spell);
        }
    }

    private void loadMonsterPreset()
    {
        loadDragons();
        loadExoskeletons();
        loadSpirits();
    }

    private void loadHeroPreset()
    {
        LoadWarriors();
        loadSorcerers();
        loadPaladins();
    }
    
    private void loadSpirits()
    {
        loadMonsterHelper("Spirits.txt");       
    }

    private void loadExoskeletons()
    {
        loadMonsterHelper("Exoskeletons.txt");
    }

    private void loadDragons()
    {
        loadMonsterHelper("Dragons.txt");
    }
    
    private void loadMonsterHelper(String fileName)
    {
        List<String> lines = loadConfigFile(fileName);
        for(int i = 1; i < lines.size(); i++)
        {
            if(lines.get(i).length() == 0)
            {
                continue;
            }
            String           line  = lines.get(i);
            List<String>     items = Arrays.asList(line.split("\\s+"));
            Iterator<String> ite   = items.iterator();
            
            String name        = !ite.hasNext() ? "???" : ite.next();
            int    level       = !ite.hasNext() ? 0     : Integer.valueOf(ite.next());
            int    strength    = !ite.hasNext() ? 0     : Integer.valueOf(ite.next());
            int    defense     = !ite.hasNext() ? 0     : Integer.valueOf(ite.next());
            int    dodgeChance = !ite.hasNext() ? 0     : Integer.valueOf(ite.next());
            
            

            Monster monster = null;
            float spicial = (float) 1.1+Utils.rand.nextFloat()/10;
            if(fileName.contains("Spirits"))
            {
                monster = new Spirit(name, level, strength, defense, dodgeChance, spicial);
            }
            if(fileName.contains("Exoskeletos"))
            {
                monster = new Exoskeleton(name, level, strength, defense, dodgeChance, spicial);
            }
            if(fileName.contains("Dragons"))
            {
                monster = new Dragon(name, level, strength, defense, dodgeChance, spicial);
            }
            this.monsterCollection.add(monster);
        }
    }

    private void loadPaladins()
    {
        loadHeroHelper("Paladins.txt");
    }

    private void loadSorcerers()
    {
        loadHeroHelper("Sorcerers.txt");
    }

    private void LoadWarriors()
    {
        loadHeroHelper("Warriors.txt");
    }
    
    private void loadHeroHelper(String fileName)
    {
        List<String> lines = loadConfigFile(fileName);
        for(int i = 1; i < lines.size(); i++)
        {
            if(lines.get(i).length() == 0)
            {
                continue;
            }
            String           line  = lines.get(i);
            List<String>     items = Arrays.asList(line.split("\\s+"));
            Iterator<String> ite   = items.iterator();
            
            String name      = !ite.hasNext() ? "???" : ite.next();
            int    mana      = !ite.hasNext() ? 0     : Integer.valueOf(ite.next());
            int    strength  = !ite.hasNext() ? 0     : Integer.valueOf(ite.next());
            int    agility   = !ite.hasNext() ? 0     : Integer.valueOf(ite.next());
            int    dexterity = !ite.hasNext() ? 0     : Integer.valueOf(ite.next());
            int    money     = !ite.hasNext() ? 0     : Integer.valueOf(ite.next());
            int    exp       = !ite.hasNext() ? 0     : Integer.valueOf(ite.next());
            int    level     = 100;

            Hero hero = null;
            if(fileName.contains("Paladin"))
            {
                hero = new Paladin(name, level, mana, money, exp, strength, dexterity, agility, (float) 1.1+Utils.rand.nextFloat()/10, (float) 1.1+Utils.rand.nextFloat()/10);
            }
            if(fileName.contains("Sorcerer"))
            {
                hero = new Sorcerer(name, level, mana, money, exp, strength, dexterity, agility, (float) 1.1+Utils.rand.nextFloat()/10, (float) 1.1+Utils.rand.nextFloat()/10);
            }
            if(fileName.contains("Warrior"))
            {
                hero = new Warrior(name, level, mana, money, exp, strength, dexterity, agility, (float) 1.1+Utils.rand.nextFloat()/10, (float) 1.1+Utils.rand.nextFloat()/10);
            }
            
            this.heroCollection.add(hero);
        }
    }
    
    public List<String> loadConfigFile(String s)
    {
        String file = System.getProperty("user.dir") + "/ConfigFiles/" + s;
        List<String> lines = Collections.emptyList();
        try {
            lines = Files.readAllLines(Paths.get(file), StandardCharsets.UTF_8); 
        } catch (IOException e) {
            System.out.println("Please enter the correct filepath");
            e.printStackTrace();
        }
        return lines;
    }
    
    private StatType getAttrInstance(String attr)
    {
        if(attr.equals(StatType.Health.toString()))
        {
            return StatType.Health;
        }
        else if(attr.equals(StatType.Mana.toString()))
        {
            return StatType.Mana;
        }
        else if(attr.equals(StatType.Agility.toString()))
        {
            return StatType.Agility;
        }
//        else if(attr.equals(StatType.Defense.toString()))
//        {
//            return StatType.Defense;
//        }
        else if(attr.equals(StatType.Dexterity.toString()))
        {
            return StatType.Dexterity;
        }
        else if(attr.equals(StatType.Strength.toString()))
        {
            return StatType.Strength;
        }
        
        return null;
    }

    public List<Armor> getArmorCollection()
    {
        return armorCollection;
    }

    public List<Weapon> getWeaponCollection()
    {
        return weaponCollection;
    }

    public List<Potion> getPotionCollection()
    {
        return potionCollection;
    }

    public List<Spell> getSpellCollection()
    {
        return spellCollection;
    }

    public void setSpellCollection(List<Spell> spellCollection)
    {
        this.spellCollection = spellCollection;
    }

    public List<Monster> getMonsterCollection()
    {
        return monsterCollection;
    }

    public List<Hero> getHeroCollection()
    {
        return heroCollection;
    }
    
    public String[] getHeroNames(String heroKind)
    {
        if(heroCollection.isEmpty())
        {
            this.loadPreset();
        }
        List<String> res = new LinkedList<>();
        for(Hero hero : heroCollection)
        {
            if(hero != null)
            {
                if((heroKind.contains("Warrior") && hero instanceof Warrior) || 
                   (heroKind.contains("Sorcerer") && hero instanceof Sorcerer) || 
                   (heroKind.contains("Paladin") && hero instanceof Paladin))
                {
                    res.add(hero.getName());
                }
            }
        }
        return res.toArray(new String[res.size()]);
    }
    
    public String[] getAllHerosNames() 
    {
        List<String> res = new LinkedList<>();
        res.addAll(Arrays.asList(getWarriorNames()));
        res.addAll(Arrays.asList(getSorcererNames()));
        res.addAll(Arrays.asList(getPaladinNames()));
        return res.toArray(new String[res.size()]);
    }
    
    public String[] getWarriorNames()
    {
        return getHeroNames("Warrior");
    }
    
    public String[] getSorcererNames()
    {
        return getHeroNames("Sorcerer");
    }
    
    public String[] getPaladinNames()
    {
        return getHeroNames("Paladin");
    }
    
    public String[] getArmorNames()
    {
        if(armorCollection.isEmpty())
        {
            this.loadPreset();
        }
        List<String> res = new LinkedList<>();
        for(Armor armor : armorCollection)
        {
            if(armor != null)
            {
                res.add(armor.getName());
            }
        }
        return res.toArray(new String[res.size()]);
    }
    
    public String[] getPortionNames()
    {
        if(potionCollection.isEmpty())
        {
            this.loadPreset();
        }
        List<String> res = new LinkedList<>();
        for(Potion potion: potionCollection)
        {
            if(potion != null)
            {
                res.add(potion.getName());
            }
        }
        return res.toArray(new String[res.size()]);
    }
    
    public String[] getWeaponNames()
    {
        if(weaponCollection.isEmpty())
        {
            this.loadPreset();
        }
        List<String> res = new LinkedList<>();
        for(Weapon weapon: weaponCollection)
        {
            if(weapon != null)
            {
                res.add(weapon.getName());
            }
        }
        return res.toArray(new String[res.size()]);
    }
    
    
}
