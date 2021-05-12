package com.picpay.domain.exceptions

abstract class DataException: RuntimeException()

class NoDataException: DataException()