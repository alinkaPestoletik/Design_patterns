#include <iostream>
#include <string>
#include <vector>
#include <map>

using namespace std;

class FormatType {
private:
    string font;
    int size;
    string color;

public:
    FormatType(const string& f, int s, const string& col) : font(f), size(s), color(col) {}

    string getFont() const { return font; }
    int getSize() const { return size; }
    string getColor() const { return color; }

    bool operator<(const FormatType& other) const {
        if (font != other.font) return font < other.font;
        if (size != other.size) return size < other.size;
        return color < other.color;
    }

    void print() const {
        cout << "Font: " << font << ", Size: " << size << ", Color: " << color;
    }
};

class FormatFactory {
private:
    static map<FormatType, FormatType*> formats;

public:
    static FormatType* getFormat(const string& font, int size, const string& color) {
        FormatType key(font, size, color);
        if (formats.find(key) == formats.end()) {
            formats[key] = new FormatType(font, size, color);
            cout << "Creating new format: ";
            formats[key]->print();
            cout << endl;
        }
        return formats[key];
    }

    static int getTotalFormats() {
        return formats.size();
    }

    static void cleanup() {
        cout << "Cleaning up " << formats.size() << " format objects." << endl;
        for (auto const& [key, val] : formats) {
            delete val;
        }
        formats.clear();
    }
};

map<FormatType, FormatType*> FormatFactory::formats;

class Character {
private:
    char character;
    FormatType* format;

public:
    Character(char c, FormatType* fmt) : character(c), format(fmt) {}

    void print() const {
        cout << "Character: " << character << " with properties: ";
        format->print();
        cout << endl;
    }
};

int main() {
    vector<Character> document;

    FormatType* redArial12 = FormatFactory::getFormat("Arial", 12, "Red");
    FormatType* blackTimes12 = FormatFactory::getFormat("Times New Roman", 12, "Black");
    FormatType* blueArial14 = FormatFactory::getFormat("Arial", 14, "Blue");
    FormatType* greenArial16 = FormatFactory::getFormat("Arial", 16, "Green");

    document.emplace_back('H', redArial12);
    document.emplace_back('e', redArial12);
    document.emplace_back('l', redArial12);
    document.emplace_back('l', redArial12);
    document.emplace_back('o', redArial12);

    document.emplace_back(' ', blackTimes12);

    document.emplace_back('W', blueArial14);
    document.emplace_back('o', blueArial14);
    document.emplace_back('r', blueArial14);
    document.emplace_back('l', blueArial14);
    document.emplace_back('d', blueArial14);

    document.emplace_back('!', greenArial16);

    cout << "\nDocument content (using Flyweight):\n";
    for (const auto& ch : document) {
        ch.print();
    }

    cout << "\nMemory Usage Analysis (using Flyweight):\n";
    cout << "Total characters: " << document.size() << endl;
    cout << "Total unique format objects created: " << FormatFactory::getTotalFormats() << endl;
    cout << "Instead of each character storing its own properties, they now share format objects.\n";
    cout << "This significantly reduces memory usage when many characters share the same formatting!\n";

    FormatFactory::cleanup();

    return 0;
}