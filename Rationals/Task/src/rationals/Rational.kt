package rationals

import java.lang.IllegalArgumentException
import java.math.BigDecimal
import java.math.BigInteger


fun main()
{
	val half = 1 divBy 2
	val third = 1 divBy 3

	val sum: Rational = half + third
	println(5 divBy 6 == sum)

	val difference: Rational = half - third
	println(1 divBy 6 == difference)

	val product: Rational = half * third
	println(1 divBy 6 == product)

	val quotient: Rational = half / third
	println(3 divBy 2 == quotient)

	val negation: Rational = -half
	println(-1 divBy 2 == negation)

	println((2 divBy 1).toString() == "2")
	println((-2 divBy 4).toString() == "-1/2")
	println("117/1098".toRational().toString() == "13/122")

	val twoThirds = 2 divBy 3
	println(half < twoThirds)

	println(half in third..twoThirds)

	println(2000000000L divBy 4000000000L == 1 divBy 2)

	println("912016490186296920119201192141970416029".toBigInteger() divBy
			"1824032980372593840238402384283940832058".toBigInteger() == 1 divBy 2)
}

data class Rational(val n: BigInteger, val d: BigInteger)
{
	init
	{
		if (d == 0.toBigInteger()) throw IllegalArgumentException()
	}

	operator fun plus(rational: Rational): Rational
	{
		val newNumerator = (this.n * rational.d) + (rational.n * this.d)
		val newDenominator = (this.d * rational.d)
		return calculateLeastCommonDenominator(newNumerator, newDenominator)
	}

	operator fun minus(rational: Rational): Rational
	{
		val newNumerator = (this.n * rational.d) - (rational.n * this.d)
		val newDenominator = (this.d * rational.d)
		return calculateLeastCommonDenominator(newNumerator, newDenominator)
	}

	operator fun times(rational: Rational): Rational
	{
		val newNumerator = (this.n * rational.n)
		val newDenominator = (this.d * rational.d)
		return calculateLeastCommonDenominator(newNumerator, newDenominator)
	}

	operator fun div(rational: Rational): Rational
	{
		val newNumerator = (this.n * rational.d)
		val newDenominator = (this.d * rational.n)
		return calculateLeastCommonDenominator(newNumerator, newDenominator)
	}

	operator fun unaryMinus(): Rational
	{
		return Rational(n.negate(), d)
	}

	operator fun compareTo(rational: Rational): Int
	{
		return this.n.times(rational.d).compareTo(rational.n.times(d))
	}

	operator fun rangeTo(rational: Rational): Pair<Rational, Rational>
	{
		return Pair(this, rational)
	}

	override fun equals(other: Any?): Boolean
	{
		if (this === other) return true
		if (other !is Rational) return false
		if (this.toString() != other.toString()) return false

		return true
	}

	override fun toString(): String
	{
		if (n == d) return "1"
		val r = calculateLeastCommonDenominator(n,d)
		var den = r.d
		var num = r.n

		if (den == 1.toBigInteger()) return "$num"
		return "$num/$den"
	}

	private fun calculateLeastCommonDenominator(numerator: BigInteger, denominator: BigInteger): Rational
	{
		val gcd = numerator.gcd(denominator)

		var den = denominator
		var num = numerator

		num /= gcd
		den /= gcd

		val numSign = num.signum()
		val denSign = den.signum()

		if ((denSign < 0 && (numSign > 0 || numSign == 1)) || (numSign < 0 && denSign < 0)){
			den = den.negate()
			num = num.negate()
		}
		return Rational(num, den)
	}

}

infix fun BigInteger.divBy(d: BigInteger): Rational = Rational(this, d)
infix fun Int.divBy(d: Int): Rational = Rational(this.toBigInteger(), d.toBigInteger())
infix fun Long.divBy(d: Long): Rational = Rational(this.toBigInteger(), d.toBigInteger())


fun String.toRational(): Rational
{
	val r = this.split("/")
	return if (r.size > 1) Rational(r[0].toBigInteger(), r[1].toBigInteger()) else return Rational(r[0].toBigInteger(), 1.toBigInteger())
}

operator fun Pair<Rational, Rational>.contains(rational: Rational): Boolean
{
	if (rational > this.first && rational < this.second) return true
	if (rational == this.first || rational == this.second) return true
	return false
}

