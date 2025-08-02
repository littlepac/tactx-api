
export interface Stock {
    id: number,
    ticker: string,
    name: string,
    previousOpen: number,
    previousClose: number,
    reasonForSelection: string,
}

export interface DayStocksView {
    stocks: [Stock],
    tradeDate: string
    tradeable: boolean
}

export interface UserDetails {
    userName: string,
    currentAccountBalance: number
    updatedTradeDate: string
}

export interface UserPick {
    pickId: number | null
}
