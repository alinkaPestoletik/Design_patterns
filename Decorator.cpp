#include <iostream>
using namespace std;

class Text {
public:
    virtual void write() = 0;
};

class PlainText : public Text {
protected:
    string text;

public:
    PlainText(string text) : text(text) {
    }

    void write() override {
        cout << text << endl;
    }
};

class TextDecorator : public PlainText {
public:
    TextDecorator(string text) : PlainText(text) {
    }

    string getText() {
        return this->text;
    }
};

class BoldDecorator : public TextDecorator {
public:
    BoldDecorator(string text): TextDecorator("\033[1m" + text + "\033[0m") {
    }

    BoldDecorator(TextDecorator *decorator): TextDecorator("\033[1m" + decorator->getText() + "\033[0m") {
    }
};

class ItalicDecorator : public TextDecorator {
public:
    ItalicDecorator(string text): TextDecorator("\033[3m" + text + "\033[0m") {
    }

    ItalicDecorator(TextDecorator *decorator): TextDecorator("\033[3m" + decorator->getText() + "\033[0m") {
    }
};

class UnderlineDecorator : public TextDecorator {
public:
    UnderlineDecorator(string text): TextDecorator("\033[4m" + text + "\033[0m") {
    }

    UnderlineDecorator(TextDecorator *decorator): TextDecorator("\033[4m" + decorator->getText() + "\033[0m") {
    }
};


int main() {
    string text1;
    cout << "enter some text:" << endl;
    getline(cin, text1);

    auto finalTextDecor = new BoldDecorator(new ItalicDecorator(new UnderlineDecorator(text1)));
    cout << finalTextDecor->getText();

    delete finalTextDecor;
}
