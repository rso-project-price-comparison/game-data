package si.fri.rso.health;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AliveSingleton {
    private boolean state = true;

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }
}