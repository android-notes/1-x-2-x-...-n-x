from fractions import Fraction


def compute_coefficients(begin, step, power):
    cur_x = begin
    points = [
        (Fraction(cur_x), Fraction(cur_x ** power))
    ]
    a = []
    b = []
    for i in range(0, power + 1):
        cur_x += step
        points.append((Fraction(cur_x), Fraction(cur_x ** power)))
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

    result_coeffs = [Fraction(0) for _ in range(power + 2)]
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


def check(begin, step, power, coeffs):
    n = 50000
    if step < 0:
        n = -n
    sum = Fraction(0)
    i = begin
    while True:
        if step < 0:
            if i < n:
                break
        else:
            if i > n:
                break
        sum += i ** power
        i += step
    n = i - step

    tmp = Fraction(0)
    p = Fraction(1)
    for i, coef in enumerate(coeffs):
        tmp += (coef * p)
        p *= n
    if sum != tmp:
        raise Exception(f"illegal, begin:{begin}, step:{step}, power:{power}, n:{n}, expected:{sum}, real:{tmp}")
    print(f'check ok, n = {float(n)}')


def main():
    # The common difference of the base can be any real number.
    step = Fraction.from_float(-0.6)
    # The minimum base can be any real number.
    begin = Fraction.from_float(-12.34)
    # The exponent can be any positive integer.
    for power in range(1, 300):
        print(f"\nbegin:{float(begin)}, step:{float(step)}, power:{power}")
        result_coeffs = compute_coefficients(begin, step, power)
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
        check(begin, step, power, result_coeffs)


if __name__ == '__main__':
    main()
