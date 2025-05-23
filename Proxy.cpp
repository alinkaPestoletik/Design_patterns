#include <iostream>
#include <string>
using namespace std;

class Document {
public:
    virtual void display() = 0;
};

class RealDocument : public Document {
private:
    string content;
public:
    RealDocument(string content) : content(content) {}
    void display() override {
        cout << "Displaying document: " << content << endl;
    }
};

class SecureDocumentProxy : public Document {
private:
    RealDocument* realDocument;
    string userRole;
public:
    SecureDocumentProxy(string content, string userRole) {
        this->realDocument = new RealDocument(content);
        this->userRole = userRole;
    }
    ~SecureDocumentProxy() {
        delete realDocument;
    }
    void display() override {
        if (userRole == "admin" || userRole == "manager") {
            realDocument->display();
        } else {
            cout << "Access denied. Insufficient permissions." << endl;
        }
    }
};

int main() {
    SecureDocumentProxy doc1("Sensitive Document Content", "admin");
    SecureDocumentProxy doc2("Sensitive Document Content", "guest");

    cout << "Admin trying to access document:" << endl;
    doc1.display();

    cout << "\nGuest trying to access document:" << endl;
    doc2.display();

    return 0;
}