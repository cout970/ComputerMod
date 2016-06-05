

/* Generic rounding macros. _size_ must be power of 2 (or result is garbage) */
#define __TRUNC(_addr_, _size_) ((_addr_) & ~((_size_) - 1))
#define __ROUND(_addr_, _size_) __TRUNC((_addr_) + (_size_) - 1, (_size_))

typedef char * va_list;

#define va_start(_ap_, _v_) \
  (_ap_ = (char*) __builtin_next_arg (_v_))
#define va_arg(_ap_, _type_) \
  ((_ap_ = (char *) ((__alignof__ (_type_) > 4 \
                       ? __ROUND((int)_ap_,8) : __ROUND((int)_ap_,4)) \
                     + __ROUND(sizeof(_type_),4))), \
   *(_type_ *) (void *) (_ap_ - __ROUND(sizeof(_type_),4)))
#define va_end(list)

#define _VA_LIST_DEFINED

