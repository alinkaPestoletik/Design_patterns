#include <iostream>
#include <vector>
#include <string>
#include <algorithm>

// EventManager class
class EventManager {
public:
    void subscribe(const std::string& eventType, void (*listener)(const std::string&)) {
        listeners[eventType].push_back(listener);
    }

    void unsubscribe(const std::string& eventType, void (*listener)(const std::string&)) {
        auto& eventListeners = listeners[eventType];
        eventListeners.erase(std::remove(eventListeners.begin(), eventListeners.end(), listener), eventListeners.end());
    }

    void notify(const std::string& eventType, const std::string& data) {
        for (auto& listener : listeners[eventType]) {
            listener(data);
        }
    }

private:
    std::map<std::string, std::vector<void (*)(const std::string&)>> listeners;
};

// Editor class
class Editor {
public:
    EventManager events;

    void openFile(const std::string& filePath) {
        std::cout << "Opening file: " << filePath << std::endl;
        events.notify("open", filePath);
    }

    void saveFile(const std::string& filePath) {
        std::cout << "Saving file: " << filePath << std::endl;
        events.notify("save", filePath);
    }
};

// Listener functions
void logOpen(const std::string& filePath) {
    std::cout << "Log: File opened - " << filePath << std::endl;
}

void logSave(const std::string& filePath) {
    std::cout << "Log: File saved - " << filePath << std::endl;
}

void backupFile(const std::string& filePath) {
    std::cout << "Backup: File backed up - " << filePath << std::endl;
}

// Demo class
int main() {
    Editor editor;

    editor.events.subscribe("open", logOpen);
    editor.events.subscribe("save", logSave);
    editor.events.subscribe("save", backupFile);

    editor.openFile("example.txt");
    editor.saveFile("example.txt");

    return 0;
}
