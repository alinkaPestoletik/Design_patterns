#include <iostream>
using namespace std;

class Light {
public:
    void on() {
        cout << "Lights are ON" << endl;
    }
    void off() {
        cout << "Lights are OFF" << endl;
    }
};

class Thermostat {
public:
    void setTemperature(string mode) {
        cout << "Thermostat set to " << mode << " mode" << endl;
    }
};

class SecurityCamera {
public:
    void activate() {
        cout << "Security cameras activated" << endl;
    }
    void deactivate() {
        cout << "Security cameras deactivated" << endl;
    }
};

class SmartHomeFacade {
private:
    Light light;
    Thermostat thermostat;
    SecurityCamera camera;
public:
    void leavingHome() {
        light.off();
        thermostat.setTemperature("eco");
        camera.activate();
    }
    void arrivingHome() {
        light.on();
        thermostat.setTemperature("comfort");
        camera.deactivate();
    }
};

int main() {
    SmartHomeFacade smartHome;
    cout << "Leaving home scenario:" << endl;
    smartHome.leavingHome();
    cout << "\nArriving home scenario:" << endl;
    smartHome.arrivingHome();
    return 0;
}