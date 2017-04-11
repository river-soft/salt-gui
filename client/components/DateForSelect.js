import React, {Component} from 'react';
import Dropdown from 'muicss/lib/react/dropdown';
import DropdownItem from 'muicss/lib/react/dropdown-item';

export default class DateForSelect extends Component {

    constructor(props) {
        super(props);

        this.state = {
            // TODO локализацию надписей
            label: 'По умолчанию'
        }
    }

    updateDataByTime(minutes, e) {

        this.props.loadData(minutes);
        this.setState({label: e.target.text});

        this.props.hideJobScriptsResult();

        let rows = document.querySelectorAll('.job-table__body_row');

        for (let i = 0; i < rows.length; i++) {
            if (rows[i].classList.contains('active')) {
                rows[i].classList.remove('active');
            }
        }
    }

    render() {
        return (
            // TODO локализацию надписей
            <Dropdown className='date-dropdown' color='primary' label={this.state.label}>
                <DropdownItem className='date-dropdown__item' onClick={(e) => {
                    ::this.updateDataByTime(30, e)
                }}>За последние 30 минут</DropdownItem>
                <DropdownItem className='date-dropdown__item' onClick={(e) => {
                    ::this.updateDataByTime(60, e)
                }}>За последний 1 час</DropdownItem>
                <DropdownItem className='date-dropdown__item' onClick={(e) => {
                    ::this.updateDataByTime(180, e)
                }}>За последний 3 часа</DropdownItem>
                <DropdownItem className='date-dropdown__item' onClick={(e) => {
                    ::this.updateDataByTime(360, e)
                }}>За последние 6 часов</DropdownItem>
                <DropdownItem className='date-dropdown__item' onClick={(e) => {
                    ::this.updateDataByTime(360, e)
                }}>За последние 12 часов</DropdownItem>
                <DropdownItem className='date-dropdown__item' onClick={(e) => {
                    ::this.updateDataByTime(1440, e)
                }}>За последние 24 часа</DropdownItem>
                <DropdownItem className='date-dropdown__item' onClick={(e) => {
                    ::this.updateDataByTime(2880, e)
                }}>За последних 2 дня</DropdownItem>
                <DropdownItem className='date-dropdown__item' onClick={(e) => {
                    ::this.updateDataByTime(10080, e)
                }}>За последних 7 дней</DropdownItem>
            </Dropdown>
        );
    }
}
