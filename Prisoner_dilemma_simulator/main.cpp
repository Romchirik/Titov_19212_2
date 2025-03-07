#include <dlfcn.h>
#include <gtest/gtest.h>

#include <iostream>
#include <memory>
#include <string>

#include "Factory/Factory.h"
#include "InputHandler/InputHandler.h"
#include "Simulator/Simulator.h"
#include "macro.h"

Factory factory;

void *load_strategy_dll(std::string &strategy_name) {
    std::unique_ptr<Strategy> (*creator)();
    std::string filename = "./../StrategiesStuff/build/";
    filename += strategy_name + ".so";

    dlerror();

    void *handle = dlopen(filename.c_str(), RTLD_LAZY);

    if (!handle) {
        return nullptr;
    }

    creator = (std::unique_ptr<Strategy>(*)())dlsym(handle, "create");
    const char *dlsym_error = dlerror();
    if (dlsym_error) {
        std::cerr << "Cannot load symbol: " << dlsym_error << '\n';
        dlclose(handle);
        return nullptr;
    }

    factory.addCreator(strategy_name, creator);
    return handle;
}

int test(int argc, char *argv[]) {
    std::vector<std::string> a = {"all-defect",
                                  "all-cooperate",
                                  "go-by-majority",
                                  "random",
                                  "soft-tit-for-tat",
                                  "tough-tit-for-tat"};

    for (auto &i : a) {
        load_strategy_dll(i);
    }

    ::testing::InitGoogleTest(&argc, argv);
    return RUN_ALL_TESTS();
}

int main(int argc, char *argv[]) {
    std::vector<void *> libs;
    InputHandler handler;
    Simulator game;
    if (!handler.parseInput(argc, argv)) {
        std::cout << "bad input, input pattern:\n"
                  << DEFAULT_INPUT_PATTERN << std::endl;
        return 0;
    }

    if (handler.testing) {
        test(argc, argv);
        exit(0);
    }

    for (auto &i : handler.strategies) {
        void *tmp = load_strategy_dll(i);
        if (!tmp) {
            std::cout << "Can't load strategy: " << i;
            return 0;
        }
        libs.push_back(tmp);
    }

    if (!game.addStrategies(handler.strategies)) {
        std::cout << game.error_message;
        return 0;
    }

    game.setMode(handler.mode);
    game.setNumSteps(handler.num_steps);
    game.startGame();

    return 0;
}