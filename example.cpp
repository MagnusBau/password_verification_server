#include <cstring>
#include <iomanip>
#include <iostream>
#include <openssl/evp.h>
#include <openssl/sha.h>
#include <sstream>
#include <string>

using namespace std;

/// Return hex string from bytes in input string.
static std::string hex(const std::string &input) {
  std::stringstream hex_stream;
  hex_stream << std::hex << std::internal << std::setfill('0');
  for (auto &byte : input)
    hex_stream << std::setw(2) << (int)(unsigned char)byte;
  return hex_stream.str();
}

const unsigned char *salt_ = reinterpret_cast<const unsigned char *>("Saltet til Ola");
const char *hash_ = "ab29d7b5c589e18b52261ecba1d3a7e7cbf212c6";
string hash_string = "ab29d7b5c589e18b52261ecba1d3a7e7cbf212c6";
int saltLen = 14;

string tryPass(string input_, int i) {
  string temp;
  if (i == 1) {
    for (int k = 65; k < 124; k++) {
      string tempString = input_ + (char)k;
      const char *newInput = tempString.c_str();
      int passLen = strlen(newInput);
      unsigned char *hash = (unsigned char *)malloc(160);
      PKCS5_PBKDF2_HMAC_SHA1(newInput, passLen, salt_, saltLen, 2048, 20, hash);
      const char *temp = reinterpret_cast<const char *>(hash);
      const string tempString2 = temp;
      string tempString3 = hex(tempString2);
      if (hash_string == tempString3) {
        return tempString;
      }
      free(hash);
    }
  } else {
    for (int k = 65; k < 124; k++) {
      string newInput = input_ + (char)k;
      cout << newInput << "*" << endl;
      temp = tryPass(newInput, i - 1);
      if (temp != "") {
        return temp;
      }
    }
    return "";
  }
  return "";
}

string getPass() {
  cout << "getPass" << endl;
  string temp = "";
  int i = 1;
  while ("" == temp) {
    temp = tryPass("", i);
    i++;
    if (i > 8) {
      break;
    }
  }
  return temp;
}


int main() {
  string password = getPass();
  cout << "Ezz game. Password friggings rekt and cracked" << endl;
  std::cout << "Password: " << password << std::endl;
}

//Utskrift:
//...
//Qu*
//Qv*
//Qw*
//Ezz game. Password friggings rekt and cracked
//Password: QwE
