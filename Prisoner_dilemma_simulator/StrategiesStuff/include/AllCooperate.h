#pragma once

#include <memory>

#include "../../macro.h"
#include "Strategy.h"

class AllCooperate : public Strategy {
   public:
    AllCooperate() = default;

    ~AllCooperate() = default;

    bool makeDecision(const std::vector<bool> &prev_d1) override;

    bool makeDecision(const std::vector<bool> &prev_d1, const std::vector<bool> &prev_d2) override;

    static std::shared_ptr<Strategy> createAllCooperate();

    void reset() override;
};
