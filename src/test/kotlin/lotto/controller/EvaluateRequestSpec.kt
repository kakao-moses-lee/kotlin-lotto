package lotto.controller

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class EvaluateRequestSpec : BehaviorSpec({
    given("우승자 번호와 보너스볼 입력값이 주어졌을 때") {
        val winningNumbersInput = "1,2, 4"
        val bonusNumberInput = "5"

        `when`("결과 계산 요청을 생성한다.") {
            val result = EvaluateRequest.from(winningNumbersInput, bonusNumberInput)

            then("결과 계산 요청이 생성된다.") {
                result.winningNumbers shouldBe listOf(1, 2, 4)
                result.bonusNumber shouldBe 5
            }
        }
    }

    given("우승자 번호를 ', '를 구분자로 구분했을 때 숫자가 아닌 입력값이 주어졌을 때") {
        val winningNumbersInput = "1,2| 3"
        val bonusNumberInput = "5"

        `when`("결과 계산 요청을 생성한다.") {
            val exception = shouldThrow<IllegalArgumentException> {
                EvaluateRequest.from(winningNumbersInput, bonusNumberInput)
            }

            then("결과 계산 요청 생성에 실패한다.") {
                exception.message shouldBe "우승자 번호는 숫자여야 합니다."
            }
        }
    }

})

class EvaluateRequest(
    val winningNumbers: List<Int>,
    val bonusNumber: Int,
) {

    companion object {
        fun from(inputWinNumbers: String, inputBonusNumber: String): EvaluateRequest {
            val winNumbers = inputWinNumbers
                .replace(" ", "")
                .split(",")
                .map { it.toIntOrNull() ?: throw IllegalArgumentException("우승자 번호는 숫자여야 합니다.") }

            val bonusNumber = inputBonusNumber.toIntOrNull() ?: throw IllegalArgumentException("보너스볼 번호는 숫자여야 합니다.")

            return EvaluateRequest(winNumbers, bonusNumber)
        }
    }
}

