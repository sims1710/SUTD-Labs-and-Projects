Sub main(

Dim total1, total2, hash1, hash2 As Decimal
total1 = (68 * (128)^7) + (69 * (128)^6) + (65 * (128)^5) + (68 * (128)^4) + (76 * (128)^3) + (73 * (128)^2) + (78 * (128)^1) + (69 * (128)^0)
total2 = (68 * (128)^7) + (68 * (128)^6) + (69 * (128)^5) + (69 * (128)^4) + (65 * (128)^3) + (76 * (128)^2) + (73 * (128)^1) + (78 * (128)^0)
hash1 = total1 % 127
hash2 = total2 % 127
Console.Writeline(hash1)
)
End Sub