#include <iostream>
#include <string>

class Handler {
public:
    virtual ~Handler() = default;
    virtual void setNext(Handler* nextHandler) = 0;
    virtual void handleRequest(const std::string& request) = 0;
};

class BaseHandler : public Handler {
protected:
    Handler* nextHandler;
public:
    BaseHandler() : nextHandler(nullptr) {}
    void setNext(Handler* nextHandler) override {
        this->nextHandler = nextHandler;
    }
    void handleRequest(const std::string& request) override {
        if (nextHandler) {
            nextHandler->handleRequest(request);
        }
    }
};

class AuthenticationHandler : public BaseHandler {
public:
    void handleRequest(const std::string& request) override {
        if (request == "authenticated") {
            std::cout << "Authentication successful.\n";
            BaseHandler::handleRequest(request);
        } else {
            std::cout << "Authentication failed.\n";
        }
    }
};

class RoleCheckHandler : public BaseHandler {
public:
    void handleRequest(const std::string& request) override {
        if (request == "admin") {
            std::cout << "Role check successful.\n";
            BaseHandler::handleRequest(request);
        } else {
            std::cout << "Role check failed.\n";
        }
    }
};

int main() {
    AuthenticationHandler authHandler;
    RoleCheckHandler roleHandler;

    authHandler.setNext(&roleHandler);

    std::string request = "authenticated";
    authHandler.handleRequest(request);

    request = "admin";
    authHandler.handleRequest(request);

    return 0;
}
