function gcd(x,y) {
    let r = null;
    if ((x < 1) || (y < 1)) {
      r = null;
    } else {
      while (x != y) {
        if (x > y) {
  let t = x - y; x = y;
  y=t
        } else {
          let t = y - x;
          y = x;
          x = t;
  } }
  r = x; }
  return r; }
  
module.exports = gcd;