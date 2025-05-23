#include <iostream>
#include <string>

class Transport {
public:
    virtual void deliver() = 0;
    virtual ~Transport() {}
};

class Truck : public Transport {
private:
    std::string address;

public:
    Truck(const std::string& address) : address(address) {}
    void deliver() override {
        std::cout << "Truck delivering to address: " << address << std::endl;
    }
};

class Ship : public Transport {
private:
    std::string country;

public:
    Ship(const std::string& country) : country(country) {}
    void deliver() override {
        std::cout << "Ship delivering to country: " << country << std::endl;
    }
};

class Plane : public Transport {
private:
    std::string country;

public:
    Plane(const std::string& country) : country(country) {}
    void deliver() override {
        std::cout << "Plane delivering to country: " << country << std::endl;
    }
};

class TransportFactory {
public:
    static Transport* getTransport(const std::string& type, const std::string& info) {
        if (type == "Truck") {
            return new Truck(info);
        } else if (type == "Ship") {
            return new Ship(info);
        } else if (type == "Plane") {
            return new Plane(info);
        }
        return nullptr;
    }
};

class FactoryDemo {
public:
    static void main() {
        Transport* truck = TransportFactory::getTransport("Truck", "123 Main St");
        if (truck != nullptr) {
            truck->deliver();
            delete truck;
        }

        Transport* ship = TransportFactory::getTransport("Ship", "USA");
        if (ship != nullptr) {
            ship->deliver();
            delete ship;
        }

        Transport* plane = TransportFactory::getTransport("Plane", "Germany");
        if (plane != nullptr) {
            plane->deliver();
            delete plane;
        }

        Transport* unknown = TransportFactory::getTransport("Car", "Some Info");
        if (unknown == nullptr) {
            std::cout << "Unknown transport type requested." << std::endl;
        }
    }
};

int main() {
    FactoryDemo::main();
    return 0;
}