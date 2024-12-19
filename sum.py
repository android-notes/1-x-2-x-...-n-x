from fractions import Fraction


def compute_coefficients(begin, step, exponent):
    cur_x = begin
    points = [
        (Fraction(cur_x), Fraction(cur_x ** exponent))
    ]
    a = []
    b = []
    for i in range(0, exponent + 1):
        cur_x += step
        points.append((Fraction(cur_x), Fraction(cur_x ** exponent)))
        # It is possible to compute a and b incrementally, optimizing the time
        # complexity to O(k), where k is the exponent.
        a_b = get_linear_regression_params(points)
        a.append((cur_x, a_b[0]))
        b.append((cur_x, a_b[1]))

    b_coeffs = lagrange_interpolation_coefficients(b[:-1])
    a_coeffs = lagrange_interpolation_coefficients(a)

    # The constant term in the sum of an arithmetic sequence.
    sn_const_term = Fraction(begin, 2) - Fraction(begin ** 2, 2 * step)
    term_count_const_term = Fraction(1) - Fraction(begin, step)

    result_coeffs = [Fraction(0) for _ in range(exponent + 2)]
    for i, coef in enumerate(b_coeffs):
        result_coeffs[i + 2] += Fraction(coef, 2 * step)
        result_coeffs[i + 1] += Fraction(coef, 2)
        result_coeffs[i] += (coef * sn_const_term)

    for i, coef in enumerate(a_coeffs):
        result_coeffs[i + 1] += Fraction(coef, step)
        result_coeffs[i] += (coef * term_count_const_term)

    return result_coeffs


# Lagrange Interpolation Method, returning the coefficients of the polynomial
# The time complexity of the Lagrange interpolation method is O(k^2), which can be reduced to
# O(klogk) with the optimization using FFT, where is the exponent.
def lagrange_interpolation_coefficients(points):
    x_values = [i[0] for i in points]
    y_values = [i[1] for i in points]
    n = len(x_values)
    coefficients = [Fraction(0)] * n

    for k in range(n):
        L = [Fraction(1)]
        for i in range(n):
            if i != k:
                new_L = [Fraction(0)] * (len(L) + 1)
                for j in range(len(L)):
                    new_L[j] += L[j] * (-x_values[i])
                    new_L[j + 1] += L[j]
                denom = x_values[k] - x_values[i]
                L = [coef / denom for coef in new_L]

        for j in range(len(L)):
            coefficients[j] += L[j] * y_values[k]

    return coefficients


def get_linear_regression_params(points: [tuple]) -> tuple:
    x = [i[0] for i in points]
    y = [i[1] for i in points]

    n = len(points)
    sum_x = sum(x)
    sum_y = sum(y)
    sum_xy = sum(x[i] * y[i] for i in range(n))
    sum_x2 = sum(x[i] ** 2 for i in range(n))

    b = (n * sum_xy - sum_x * sum_y) / (n * sum_x2 - sum_x ** 2)
    a = (sum_y - b * sum_x) / n
    return a, b


def check(begin, step, exponent, coeffs):
    # Mathematical Induction
    # check n=begin
    sum1 = 0
    for i, coef in enumerate(reversed(coeffs)):
        index = len(coeffs) - i - 1
        sum1 = sum1 + coef * begin ** index

    sum2 = begin ** exponent
    if sum1 != sum2:
        raise Exception()

    # check n and n+step
    import sympy as sp
    n = sp.symbols('n')
    expr = 0
    for i, coef in enumerate(reversed(coeffs)):
        index = len(coeffs) - i - 1
        pre = coef * ((n - step) ** index)
        cur = coef * (n ** index)
        expr = expr + (cur - pre)

    if not expr.equals(n ** exponent):
        raise Exception()
    print("Inductive proof: ", sp.expand(expr))


def main():
    # The common difference of the base can be any real number, including decimals, negative numbers, etc.
    step = Fraction.from_float(1)
    # The minimum base can be any real number, including decimals, negative numbers, etc.
    begin = Fraction.from_float(1)
    # The exponent can be any positive integer.
    for exponent in range(1, 20):
        print(f"\nbegin:{float(begin)}, step:{float(step)}, exponent:{exponent}")
        result_coeffs = compute_coefficients(begin, step, exponent)
        for i, coef in enumerate(reversed(result_coeffs)):
            # print(f"{len(result_coeffs) - i - 1}: {coef}   ", end='')
            index = len(result_coeffs) - i - 1
            if coef == 0 and index != 0:
                continue
            if index > 1:
                print(f"{coef}*n^{index} + ", end='')
            elif index == 1:
                print(f"{coef}*n + ", end='')
            else:
                print(f"{coef} ", end='')
        print()
        check(begin, step, exponent, result_coeffs)


if __name__ == '__main__':
    main()
