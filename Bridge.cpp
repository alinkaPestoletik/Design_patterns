#include <iostream>
#include <string>

using namespace std;

class MusicPlayer {
protected:
    class AudioCodec* codec; 
public:
    MusicPlayer(AudioCodec* codec) : codec(codec) {}
    virtual void play(const string& fileName) = 0;
    virtual ~MusicPlayer() {}
};

class AudioCodec {
public:
    virtual void decode(const string& fileName) = 0;
    virtual ~AudioCodec() {}
};

class MP3Codec : public AudioCodec {
public:
    void decode(const string& fileName) override {
        cout << "Decoding " << fileName << " using MP3 codec." << endl;
    }
};

class WAVCodec : public AudioCodec {
public:
    void decode(const string& fileName) override {
        cout << "Decoding " << fileName << " using WAV codec." << endl;
    }
};

class FLACCodec : public AudioCodec {
public:
    void decode(const string& fileName) override {
        cout << "Decoding " << fileName << " using FLAC codec." << endl;
    }
};

class PopMusicPlayer : public MusicPlayer {
public:
    PopMusicPlayer(AudioCodec* codec) : MusicPlayer(codec) {}
    void play(const string& fileName) override {
        cout << "Started Pop Player..." << endl;
        codec->decode(fileName);
    }
};

class JazzMusicPlayer : public MusicPlayer {
public:
    JazzMusicPlayer(AudioCodec* codec) : MusicPlayer(codec) {}
    void play(const string& fileName) override {
        cout << "Started Jazz Player..." << endl;
        codec->decode(fileName);
    }
};

class RockMusicPlayer : public MusicPlayer {
public:
    RockMusicPlayer(AudioCodec* codec) : MusicPlayer(codec) {}
    void play(const string& fileName) override {
        cout << "Started Rock Player..." << endl;
        codec->decode(fileName);
    }
};

int main() {
    AudioCodec* mp3Codec = new MP3Codec();
    AudioCodec* wavCodec = new WAVCodec();
    AudioCodec* flacCodec = new FLACCodec();

    MusicPlayer* popPlayerMP3 = new PopMusicPlayer(mp3Codec);
    MusicPlayer* jazzPlayerWAV = new JazzMusicPlayer(wavCodec);
    MusicPlayer* rockPlayerFLAC = new RockMusicPlayer(flacCodec);
    MusicPlayer* popPlayerWAV = new PopMusicPlayer(wavCodec);

    popPlayerMP3->play("pop_hit.mp3");
    jazzPlayerWAV->play("smooth_jazz.wav");
    rockPlayerFLAC->play("rock_anthem.flac");
    popPlayerWAV->play("another_pop.wav");

    delete popPlayerMP3;
    delete jazzPlayerWAV;
    delete rockPlayerFLAC;
    delete popPlayerWAV;
    delete mp3Codec;
    delete wavCodec;
    delete flacCodec;

    return 0;
}