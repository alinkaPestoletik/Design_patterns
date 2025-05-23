#include <iostream>
#include <string>
#include <vector>
#include <fstream>

using namespace std;

class Enemy {
private:
    string name;
    int hp;
    bool alive;
public:
    Enemy(string name, int hp) : name(name), hp(hp), alive(true) {}

    void displayHp() {
        cout << hp << endl;
    }

    void decreaseStrength(int damage) {
        if (alive) {
            hp -= damage;
            hp = max(0, hp);
            if (hp <= 0) {
                alive = false;
            }
        } else {
            cout << "The enemy is dead!" << endl;
        }
    }

    string getName() {
        return this->name;
    }
};

class AttackStrategy {
public:
    virtual void attack(Enemy &enemy) = 0;
};

class WitchAttackStrategy : public AttackStrategy {
public:
    virtual void attack(Enemy &enemy) = 0;
};

class KnightAttackStrategy : public AttackStrategy {
public:
    virtual void attack(Enemy &enemy) = 0;
};

class ArcherAttackStrategy : public AttackStrategy {
public:
    virtual void attack(Enemy &enemy) = 0;
};


class FireMagicAtackStrategy : public WitchAttackStrategy {
public:
    void attack(Enemy &enemy) override {
        cout << "Fireball is trown the damage is 50" << endl;
        enemy.decreaseStrength(50);
    }
};

class LightningMagicAtackStrategy : public WitchAttackStrategy {
public:
    void attack(Enemy &enemy) override {
        cout << "Wzzap! 30 damage dealt" << endl;
        enemy.decreaseStrength(30);
    }
};

class HelbardKnightAttackStrategy : public KnightAttackStrategy {
public:
    void attack(Enemy &enemy) override {
        cout << "Lunge-strike-success! 5 damage dealt" << endl;
        enemy.decreaseStrength(5);
    }
};

class SwordKnightAttackStrategy : public KnightAttackStrategy {
public:
    void attack(Enemy &enemy) override {
        cout << "Slash! Perferct hit! 10 damage dealt";
        enemy.decreaseStrength(10);
    }
};

class ArrowArcherAttackStrategy : public ArcherAttackStrategy {
public:
    void attack(Enemy &enemy) override {
        cout << "Schwoof! 10 damage dealt" << endl;
        enemy.decreaseStrength(10);
    }
};

class FistsArcherAttackStrategy : public ArcherAttackStrategy {
public:
    void attack(Enemy &enemy) override {
        cout << "Trying to use fists! 1 damage dealt" << endl;
        enemy.decreaseStrength(1);
    }
};

class Character {
protected:
    string name;

    Character(string name) : name(name) {}
};


class Witch : public Character {
private:
    WitchAttackStrategy *strategy;
public:
    Witch(string name, WitchAttackStrategy *attackStrategy) : Character(name), strategy(attackStrategy) {}

    void magicAttack(Enemy &enemy) {
        strategy->attack(enemy);
    }

    void changeStrategy(WitchAttackStrategy *newStrategy) {
        strategy = newStrategy;
    }
};

class Knight : public Character {
private:
    KnightAttackStrategy *strategy;
public:
    Knight(string name, KnightAttackStrategy *attackStrategy) : Character(name), strategy(attackStrategy) {}

    void meleeAttack(Enemy &enemy) {
        strategy->attack(enemy);
    }

    void changeStrategy(KnightAttackStrategy *newStrategy) {
        strategy = newStrategy;
    }
};


class Archer : public Character {
private:
    ArcherAttackStrategy *strategy;
public:
    Archer(string name, ArcherAttackStrategy *attackStrategy): Character(name), strategy(attackStrategy) {}

    void rangeAttack(Enemy &enemy) {
        strategy->attack(enemy);
    }

    void changeStrategy(ArcherAttackStrategy *newStrategy) {
        strategy = newStrategy;
    }
};


int main() {
    Archer archer("Albert", new ArrowArcherAttackStrategy);
    Witch witch("Emma", new FireMagicAtackStrategy);
    Knight knight("Richard", new HelbardKnightAttackStrategy);
    Enemy troll("Troll", 1000);
    archer.rangeAttack(troll);
    troll.displayHp();

    witch.magicAttack(troll);
    troll.displayHp();

    knight.meleeAttack(troll);
    troll.displayHp();

    return 0;
}
