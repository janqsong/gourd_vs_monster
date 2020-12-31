package com.sjq.gourd.collision;

import com.sjq.gourd.bullet.Bullet;
import com.sjq.gourd.bullet.BulletState;
import com.sjq.gourd.constant.Constant;
import com.sjq.gourd.constant.ImageUrl;
import com.sjq.gourd.creature.Creature;
import com.sjq.gourd.creature.CreatureState;
import com.sjq.gourd.creature.CreatureStateWithClock;

public class Collision {
    private final Bullet bullet;

    public Collision(Bullet bullet) {
        this.bullet = bullet;
    }

    public void collisionEvent() {
        Creature targetCreature = bullet.getTargetCreature();
        Creature sourceCreature = bullet.getSourceCreature();

        if (bullet.getBulletState().equals(BulletState.THE_GOD_OF_HEALING)) {
            //爷爷的普通子弹,给自己方加血,加血量等于爷爷的攻击力
            targetCreature.setCurrentHealth(targetCreature.getCurrentHealth() + sourceCreature.getCurrentAttack());
            bullet.setValid(false);
            bullet.setVisible(false);
            sourceCreature.setCurrentMagic(sourceCreature.getCurrentMagic() + sourceCreature.getMagicIncrementOnce());
            return;
        } else if (bullet.getBulletState().equals(BulletState.THE_GOD_OF_HEALING_MAX)) {
            //爷爷的技能,加血500,治愈buff持续5000ms
            targetCreature.setCurrentHealth(targetCreature.getCurrentHealth() + 500);
            targetCreature.getStateSet().add(new CreatureStateWithClock(CreatureState.CURE, 5000));
            //5s的治愈效果
            bullet.setValid(false);
            bullet.setVisible(false);
            return;
        }

        //除了爷爷的子弹,其他的所有子弹,都需要计算伤害
        double targetHealth = targetCreature.getCurrentHealth();
        double sourceAttack = sourceCreature.getCurrentAttack();
        double targetDefense = targetCreature.getCurrentDefense();
        double damage = sourceAttack - targetDefense;
        //伤害小于1强制等于1
        if (damage < 1)
            damage = 1;
        damage = (double) Math.round(damage);
        targetCreature.setCurrentHealth(targetHealth - damage);
        //碰撞发生,子弹无效,不可见
        bullet.setValid(false);
        bullet.setVisible(false);

        //子弹是普通子弹的时候才增加蓝量,否则会导致技能的多颗子弹加蓝量,致使无限技能
        if (bullet.getBulletState().equals(BulletState.NONE))
            sourceCreature.setCurrentMagic(sourceCreature.getCurrentMagic() + sourceCreature.getMagicIncrementOnce());

        //冰冻2000ms,灼烧3000ms,重伤5000ms,致残5000ms
        if (bullet.getBulletState().equals(BulletState.THE_HEART_OF_ICE)) {
            targetCreature.addState(new CreatureStateWithClock(CreatureState.FROZEN, 2000));
        } else if (bullet.getBulletState().equals(BulletState.THE_SON_OF_FLAME)) {
            targetCreature.addState(new CreatureStateWithClock(CreatureState.FIRING, 3000));
        } else if (bullet.getBulletState().equals(BulletState.THE_TEETH_OF_POISONOUS)
                || bullet.getBulletState().equals(BulletState.GAZE_OF_MAGIC_MIRROR)) {
            targetCreature.addState(new CreatureStateWithClock(CreatureState.SERIOUS_INJURY, 5000));
        } else if (bullet.getBulletState().equals(BulletState.SPEED_CUT_CLAW)) {
            targetCreature.addState(new CreatureStateWithClock(CreatureState.SPEED_CUT, 5000));
        }

        //近战子弹联网实现和单机不同,爪子的显示不在碰撞执行函数中写
//        if (bullet.getBulletType() == Constant.CLOSE_BULLET_TYPE) {
//            if (sourceCreature.getClawType() != Constant.ClawType.NONE_CLAW && targetCreature.isAlive()) {
////                targetCreature.getCloseAttackImageView().setImage(ImageUrl.closeAttackImageMap.get(sourceCreature.getClawType()));
////                bullet.getTargetCreature().setLastCloseAttack(System.currentTimeMillis());
//            }
//        } else {
//
//        }
    }
}