import React, {Component} from 'react';
import Dropdown from 'muicss/lib/react/dropdown';
import DropdownItem from 'muicss/lib/react/dropdown-item';

export default class DateForSelect extends Component {

    constructor(props) {
        super(props);

        this.state = {
            label: 'По умолчанию'
        }
    }

    test(minutes, e) {

        this.props.loadData(minutes);
        this.setState({label: e.target.text});
    }

    render() {
        return (
            <Dropdown className='date-dropdown' color='primary' label={this.state.label}>
                <DropdownItem className='date-dropdown__item' onClick={(e) => {
                    ::this.test(30, e)
                }}>За последние 30 минут</DropdownItem>
                <DropdownItem className='date-dropdown__item' onClick={(e) => {
                    ::this.test(60, e)
                }}>За последний 1 час</DropdownItem>
                <DropdownItem className='date-dropdown__item' onClick={(e) => {
                    ::this.test(180, e)
                }}>За последний 3 часа</DropdownItem>
                <DropdownItem className='date-dropdown__item' onClick={(e) => {
                    ::this.test(360, e)
                }}>За последние 6 часов</DropdownItem>
                <DropdownItem className='date-dropdown__item' onClick={(e) => {
                    ::this.test(360, e)
                }}>За последние 12 часов</DropdownItem>
                <DropdownItem className='date-dropdown__item' onClick={(e) => {
                    ::this.test(1440, e)
                }}>За последние 24 часа</DropdownItem>
                <DropdownItem className='date-dropdown__item' onClick={(e) => {
                    ::this.test(2880, e)
                }}>За последних 2 дня</DropdownItem>
                <DropdownItem className='date-dropdown__item' onClick={(e) => {
                    ::this.test(10080, e)
                }}>За последних 7 дней</DropdownItem>
            </Dropdown>
        );
    }
}
